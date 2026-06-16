import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Contact, ContactService } from '../services/contact-service';

@Component({
  selector: 'app-contact-form',
  standalone: false,
  templateUrl: './contact-form.html',
  styleUrl: './contact-form.scss',
})
export class ContactForm {
  @Input() action: 'create' | 'update' = 'update';
  @Input() contactId?: number;
  @Output() contactFormClosed = new EventEmitter<number|undefined>();
  isUpdate: boolean | undefined;
  actualContact: Contact | undefined;

  constructor(private contactService: ContactService) {}


  ngOnInit() {
    this.isUpdate = this.action === 'update'; 
    
    if (this.isUpdate && this.contactId !== undefined) {
      this.contactService.getContactById(this.contactId).subscribe({
        next: (contact => this.actualContact = contact),
        error: (err: Error) => {
          this.actualContact = undefined;
          console.error(err.message);
        }
      })
    }  
  }

  closeForm() {
    this.contactFormClosed.emit(undefined);
  }

  submitContactForm(contactData: Omit<Contact, 'id'>) {
    if (this.isUpdate && this.contactId !== undefined) {
      this.contactService.updateContact(this.contactId, contactData).subscribe({
        next: () => {
          console.log('Contact updated successfully');
          this.contactFormClosed.emit(this.contactId);
        },
        error: (err: Error) => {
          console.error('Error updating contact:', err.message);
          this.contactFormClosed.emit(undefined);
        }
      });
    } else {
      this.contactService.createContact(contactData).subscribe({
        next: (newContactId) => {
          console.log('Contact created successfully');
          this.contactFormClosed.emit(newContactId);
        },
        error: (err: Error) => {
          console.error('Error creating contact:', err.message);
          this.contactFormClosed.emit(undefined);
        }
      });
    }
  }
}
