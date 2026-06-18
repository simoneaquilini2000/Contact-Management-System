import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { HttpClient } from '@angular/common/http';


export interface Contact {
  id: number;
  name: string;
  surname: string;
  phone: string;
}

@Injectable({
  providedIn: 'root',
})
export class ContactService {

  constructor(private client: HttpClient, private authService: AuthService) {}

  private getContactsApiUrl(): string {
    return this.authService.getConfig().apiUrl;
  }

  private checkUserLoggedIn() {
    if (!this.authService.isLoggedIn()) {
      throw new Error('User is not logged in');
    }
  }

  getAllContacts() {
    this.checkUserLoggedIn();
    return this.client.get<Contact[]>(`${this.getContactsApiUrl()}/api/contacts`);
  }

  getContactById(id: number) {
    this.checkUserLoggedIn();
    return this.client.get<Contact>(`${this.getContactsApiUrl()}/api/contacts/${id}`);
  }

  createContact(contact: Omit<Contact, 'id'>) {
    this.checkUserLoggedIn();
    return this.client.post<number>(`${this.getContactsApiUrl()}/api/contacts`, contact);
  }

  updateContact(id: number, contact: Omit<Contact, 'id'>) {
    this.checkUserLoggedIn();
    return this.client.put(`${this.getContactsApiUrl()}/api/contacts/${id}`, contact);
  }

  deleteContact(id: number) {
    this.checkUserLoggedIn();
    return this.client.delete(`${this.getContactsApiUrl()}/api/contacts/${id}`, {
      responseType: 'text'
    });
  }
}
