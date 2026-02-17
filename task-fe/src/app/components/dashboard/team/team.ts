import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { accessCode, Company, Page, User } from '../../../interfaces/interfaces';
import { CompanyService } from '../../../services/company.service';
import { AuthService } from '../../../services/auth.service';
import { DatePipe, NgClass } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { Tooltip, TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-team',
  imports: [DatePipe, ReactiveFormsModule, NgClass, ToastModule, Tooltip],
  templateUrl: './team.html',
  styleUrl: './team.scss',
  providers: [MessageService, NgClass, TooltipModule],
})
export class TeamComponent implements OnInit {
  protected users!: Page<User>;
  protected companyService: CompanyService = inject(CompanyService);
  protected authService: AuthService = inject(AuthService);
  protected company: Company = this.authService.getCompany();
  protected addSomeone: boolean = false;
  protected addForm: FormGroup = new FormGroup({});
  protected messageService: MessageService = inject(MessageService);
  protected accessCode: string = '';
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected searchUsersForm: FormGroup = new FormGroup({});
  ngOnInit(): void {
    this.companyService.getUsers(this.company.id, true).subscribe({
      next: (data: Page<User>) => {
        this.users = data;
        this.cdr.detectChanges();
      },
    });
    this.searchUsersForm = new FormGroup({
      search: new FormControl(''),
    });
    this.addForm = new FormGroup({
      email: new FormControl('', [
        Validators.required,
        Validators.pattern(/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/),
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/),
      ]),
      nome: new FormControl('', [Validators.required]),
      cognome: new FormControl('', [Validators.required]),
    });
  }

  add() {
    if (this.addForm.valid) {
      let body = {
        email: this.addForm.controls['email'].value,
        password: this.addForm.controls['password'].value,
        nome: this.addForm.controls['nome'].value,
        cognome: this.addForm.controls['cognome'].value,
        companyId: this.company!.id,
      };
      this.companyService.addUser(body).subscribe({
        next: (data: accessCode) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Added!',
            detail: 'User added succesfuly! Now share the code with him!',
            life: 3000,
          });
          this.accessCode = data?.code;
          this.companyService.getUsers(this.company.id, true).subscribe({
            next: (users: Page<User>) => {
              this.users = users;
              this.cdr.markForCheck();
            },
          });
        },
      });
    }
  }

  manageAdd() {
    this.addSomeone = !this.addSomeone;
    if (!this.addSomeone) {
      if (this.accessCode) {
        this.addForm.reset();
        this.accessCode = '';
      }
    }
  }
  getInitials(user: User): string {
    return (
      user.nome.substring(0, 1).toUpperCase() + ' ' + user.cognome.substring(0, 1).toUpperCase()
    );
  }
}
