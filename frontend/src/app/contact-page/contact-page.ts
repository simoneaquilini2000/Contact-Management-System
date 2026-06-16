import { Component } from '@angular/core';
import { Contact, ContactService } from '../services/contact-service';
import { UserInfo, UserService } from '../services/user-service';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-contact-page',
  standalone: false,
  templateUrl: './contact-page.html',
  styleUrl: './contact-page.scss',
})
export class ContactPage {
  userInfo: UserInfo | undefined
  userContacts: Contact[] = []
  error = ''
  contactFormEnabled = false;
  selectedContactId?: number | undefined;

  constructor(private userService: UserService, private contactService: ContactService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    
    this.userService.getUserInfo().subscribe( {
      next: (info => this.userInfo = info),
      error: (err: Error) => {
        this.userInfo = undefined;
        this.error = err.message;
      }
    })

    this.getAllContacts();
  }

  getAllContacts(){
    this.contactService.getAllContacts().subscribe( {
      next: (contacts => this.userContacts = contacts),
      error: (err: Error) => {
        this.userContacts = [];
        this.error = err.message;
      }
    })
  }

  selectContact(contactId: number) {
    this.selectedContactId = contactId;
  }

  openContactForm() {
    this.contactFormEnabled = true;
  }

  closeContactForm() {
    this.contactFormEnabled = false;
    this.selectedContactId = undefined;
  }

  onClosedContactForm(contactId?: number) {
    this.closeContactForm();
    if (contactId !== undefined) {
      this.getAllContacts(); // Refresh the contact list after creating or updating a contact
    }
  }

  deleteContact(contactId: number) {
    this.contactService.deleteContact(contactId).subscribe({
      next: () => {
        console.log('Contact deleted successfully');
        this.getAllContacts(); // Refresh the contact list after deletion
      },
      error: (err: Error) => {
        console.error('Error deleting contact:', err.message);
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
