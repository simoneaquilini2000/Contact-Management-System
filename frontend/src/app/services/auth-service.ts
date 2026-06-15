import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

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

  private readonly keycloakUrl = 'http://localhost:8180'; // TODO: take Keycloak data from ENV
  private readonly realm = 'contact-management-system';
  private readonly clientId = 'contact-management-spring-boot';
  private readonly clientSecret = 'GZblE8M3oCC0gSqrYWLrNcIpK1ialWEQ';

  private get tokenEndpoint(): string {
    return `${this.keycloakUrl}/realms/${this.realm}/protocol/openid-connect/token`;
  }

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<TokenResponse> {
    const body = new URLSearchParams({
      grant_type: 'password',
      client_id: this.clientId,
      client_secret: this.clientSecret,
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