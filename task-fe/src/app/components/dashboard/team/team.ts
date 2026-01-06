import { Component, inject, OnInit } from '@angular/core';
import { Company, Page, User } from '../../../interfaces/interfaces';
import { CompanyService } from '../../../services/company.service';
import { AuthService } from '../../../services/auth.service';
import { DatePipe } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-team',
  imports: [DatePipe, ReactiveFormsModule],
  templateUrl: './team.html',
  styleUrl: './team.scss',
})
export class TeamComponent implements OnInit {
  protected users!: Page<User>;
  protected companyService: CompanyService = inject(CompanyService);
  protected authService: AuthService = inject(AuthService);
  protected company: Company = this.authService.getCompany();
  protected addSomeone: boolean = false;
  protected addForm: FormGroup = new FormGroup({});
  ngOnInit(): void {
    this.companyService.getUsers(this.company.id).subscribe({
      next: (data: Page<User>) => {
        this.users = data;
      },
    });
  }
}
