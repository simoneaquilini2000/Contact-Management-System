import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export interface UserInfo{
  name: string,
  email: string
}

@Injectable({
  providedIn: 'root',
})
export class UserService {

  constructor(private client: HttpClient, private authService: AuthService) {}

  private getUserApiUrl(): string {
    return this.authService.getConfig().apiUrl;
  }

  getUserInfo() {

    // Assuming the AuthService has a method to get user info
    return this.client.get<UserInfo>(`${this.getUserApiUrl()}/api/user`).pipe(
      catchError(err => {
              const message = `Couldn't fetch user data`;
              console.log(message)
              return throwError(() => new Error(message));
            }
    ))
  }

}
