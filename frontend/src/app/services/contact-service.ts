import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { HttpClient } from '@angular/common/http';


export interface Contact {
  id: number;
  name: string;
  email: string;
  // phone: string;
}

@Injectable({
  providedIn: 'root',
})
export class ContactService {

  private readonly contactApiEndpoint = "http://localhost:8080"

  constructor(private client: HttpClient, private authService: AuthService) {}

  private checkUserLoggedIn() {
    if (!this.authService.isLoggedIn()) {
      throw new Error('User is not logged in');
    }
  }

  getAllContacts() {
    this.checkUserLoggedIn();
    return this.client.get<Contact[]>(`${this.contactApiEndpoint}/api/contacts`);
  }

  getContactById(id: number) {
    this.checkUserLoggedIn();
    return this.client.get<Contact>(`${this.contactApiEndpoint}/api/contacts/${id}`);
  }

  createContact(contact: Omit<Contact, 'id'>) {
    this.checkUserLoggedIn();
    return this.client.post<number>(`${this.contactApiEndpoint}/api/contacts`, contact);
  }

  updateContact(id: number, contact: Omit<Contact, 'id'>) {
    this.checkUserLoggedIn();
    return this.client.put(`${this.contactApiEndpoint}/api/contacts/${id}`, contact);
  }

  deleteContact(id: number) {
    this.checkUserLoggedIn();
    return this.client.delete(`${this.contactApiEndpoint}/api/contacts/${id}`);
  }
}
