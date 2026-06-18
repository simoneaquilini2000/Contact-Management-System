import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Contact, ContactService } from '../services/contact-service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-contact-form',
  standalone: false,
  templateUrl: './contact-form.html',
  styleUrl: './contact-form.scss',
})
export class ContactForm {
  @Input() action: 'create' | 'update' = 'update';
  @Input() contactId?: number;
  @Output() closedContactForm = new EventEmitter<number|undefined>();
  isUpdate: boolean | undefined;
  actualContact: Contact | undefined;
  loading = false;
  contactForm!: FormGroup;

  constructor(private fb: FormBuilder,
              private contactService: ContactService) {}


  ngOnInit() {

    this.contactForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(1), Validators.pattern(/^[a-zA-Z]+$/)]],
      surname: ['', [Validators.required, Validators.minLength(1), Validators.pattern(/^[a-zA-Z]+$/)]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]]
    });
  
    this.isUpdate = this.action === 'update'; 
    
    if (this.isUpdate && this.contactId !== undefined) {
      this.contactService.getContactById(this.contactId).subscribe({
        next: (contact => {
          this.actualContact = contact;
          this.contactForm.setValue({
            name: contact.name,
            surname: contact.surname,
            phone: contact.phone
          });
        }),
        error: (err: Error) => {
          this.actualContact = undefined;
          console.error(err.message);
        }
      })
    }  
  }

  closeForm() {
    this.closedContactForm.emit(undefined);
  }

  submitContactForm(contactData: Omit<Contact, 'id'>) {
    if (this.contactForm.invalid){ 
      console.log('Form is invalid');
      return;
    }

    this.loading = true;
    if (this.isUpdate && this.contactId !== undefined) {
      this.contactService.updateContact(this.contactId, contactData).subscribe({
        next: () => {
          console.log('Contact updated successfully');
          this.closedContactForm.emit(this.contactId);
        },
        error: (err: Error) => {
          console.error('Error updating contact:', err.message);
          this.closedContactForm.emit(undefined);
        }
      });
    } else {
      this.contactService.createContact(contactData).subscribe({
        next: (newContactId) => {
          console.log('Contact created successfully');
          this.closedContactForm.emit(newContactId);
        },
        error: (err: Error) => {
          console.error('Error creating contact:', err.message);
          this.closedContactForm.emit(undefined);
        }
      });
    }
    this.loading = false;
  }
}
