import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export interface UserInfo{
  name: String,
  email: String
}

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private readonly userApiEndpoint = "http://localhost:8080"

  constructor(private client: HttpClient, private authService: AuthService) {}

  getUserInfo() {
    if (!this.authService.isLoggedIn()) {
      throw new Error('User is not logged in');
    }

    // Assuming the AuthService has a method to get user info
    return this.client.get<UserInfo>(`${this.userApiEndpoint}/api/user`).pipe(
      catchError(err => {
              const message = `Couldn't fetch user data`;
              console.log(message)
              return throwError(() => new Error(message));
            }
    ))
  }

}
