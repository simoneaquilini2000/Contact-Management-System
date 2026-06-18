import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AppConfig, ConfigService } from './config-service';

export interface TokenResponse {
  access_token: string;
  refresh_token: string;
  expires_in: number;
  token_type: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'access_token';
  private readonly REFRESH_KEY = 'refresh_token';
  private appConfig!: AppConfig;

  constructor(private http: HttpClient, private configService: ConfigService, private router: Router) {}

  getConfig(): AppConfig {
    this.appConfig = this.configService.get();
    return this.appConfig;
  }

  private get tokenEndpoint(): string {
    return `${this.getConfig().keycloakUrl}/realms/${this.getConfig().keycloakRealm}/protocol/openid-connect/token`;
  }

  login(email: string, password: string): Observable<TokenResponse> {
    const body = new URLSearchParams({
      grant_type: 'password',
      client_id: this.getConfig().keycloakClientId,
      client_secret: this.getConfig().keycloakClientSecret,
      username: email,
      password: password
    });

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });

    return this.http.post<TokenResponse>(this.tokenEndpoint, body.toString(), { headers }).pipe(
      tap(tokens => this.storeTokens(tokens)),
      catchError(err => {
        const message = err.status === 401
          ? 'Invalid username or password'
          : 'Login failed, please try again';
        return throwError(() => new Error(message));
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_KEY);
    this.router.navigate(['/login']);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getAccessToken();
  }

  private storeTokens(tokens: TokenResponse): void {
    localStorage.setItem(this.TOKEN_KEY, tokens.access_token);
    localStorage.setItem(this.REFRESH_KEY, tokens.refresh_token);
  }
}