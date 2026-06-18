import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface AppConfig {
  keycloakUrl: string;
  keycloakRealm: string;
  keycloakClientId: string;
  keycloakClientSecret: string;
  apiUrl: string;
}

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private config!: AppConfig;

  constructor(private http: HttpClient) {}

  loadConfig(): Promise<void> {
    return firstValueFrom(
      this.http.get<AppConfig>('/config.json')
    ).then(config => {
      console.log('Config loaded:', config);
      this.config = config;
    })
    .catch(err => {
      console.error('Failed to load config:', err);
      return Promise.reject(err);
    });
  }

  get(): AppConfig {
    return this.config;
  }
}
