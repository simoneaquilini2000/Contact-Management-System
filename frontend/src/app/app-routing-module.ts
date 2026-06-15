import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth-guard';
import { LoginComponent } from './components/login/login.component';
import { CardList } from './components/card-list/card-list';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'cards', component: CardList, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
