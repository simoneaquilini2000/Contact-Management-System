import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { LoginComponent } from './components/login/login.component';
import { CardList } from './components/card-list/card-list';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [App, LoginComponent, CardList],
  imports: [BrowserModule, AppRoutingModule,
    ReactiveFormsModule],
  providers: [provideBrowserGlobalErrorListeners(), provideClientHydration(withEventReplay()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [App],
})
export class AppModule {}
