import { ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
import { Contact, ContactService } from '../services/contact-service';
import { UserInfo, UserService } from '../services/user-service';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-contact-page',
  standalone: false,
  templateUrl: './contact-page.html',
  styleUrl: './contact-page.scss',
})
export class ContactPage implements OnInit {
  userInfo: UserInfo | undefined
  userContacts: Contact[] = []
  error = ''
  contactFormEnabled = false;
  action: 'create' | 'update' = 'update';
  selectedContactId?: number | undefined;

  constructor(private userService: UserService, private contactService: ContactService,
    private authService: AuthService, private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.userService.getUserInfo().subscribe({
        next: (info) => {
          this.userInfo = info;
          this.cdr.detectChanges();
        }
    });

    this.getAllContacts();
  }

  getAllContacts(){
    this.contactService.getAllContacts().subscribe( {
      next: (contacts => {
        this.userContacts = contacts;
        this.cdr.detectChanges();
      }),
      error: (err: Error) => {
        this.userContacts = [];
        this.error = err.message;
        this.cdr.detectChanges(); 
      }
    })
  }

  selectContact(contactId: number) {
    this.selectedContactId = contactId;
  }

  openContactForm(action: 'create' | 'update') {
    this.action = action;
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

  deleteContact(contactId: number|undefined) {
    if (contactId === undefined) return;
    this.contactService.deleteContact(contactId).subscribe({
      next: () => {
        console.log('Contact deleted successfully');
        this.selectedContactId = undefined;
        this.getAllContacts(); // Refresh the contact list after deletion
      },
      error: (err: Error) => {
        console.error('Error deleting contact:', err.message);
        this.cdr.detectChanges(); 
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
