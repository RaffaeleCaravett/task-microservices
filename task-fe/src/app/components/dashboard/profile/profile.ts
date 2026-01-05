import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { MenuService } from '../../../services/menu.service';
import { filter, map } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { Company, project, User } from '../../../interfaces/interfaces';
import { DatePipe, NgClass } from '@angular/common';
import { CompanyService } from '../../../services/company.service';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Table } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { MultiSelectModule } from 'primeng/multiselect';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
@Component({
  selector: 'app-profile',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    TableModule,
    TagModule,
    IconFieldModule,
    InputTextModule,
    InputIconModule,
    MultiSelectModule,
    SelectModule,
    FormsModule,
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class ProfileComponent implements OnInit {
  protected menuService: MenuService = inject(MenuService);
  protected companyService: CompanyService = inject(CompanyService);
  protected authService: AuthService = inject(AuthService);
  protected section: string = 'Generale';
  protected user: User | null = null;
  protected company: Company | null = null;
  protected users: User[] = [];
  protected projectsToShow: project[] = [];
  protected searchProjectForm = new FormGroup({
    search: new FormControl(''),
  });
  status: string[] = ['ACTIVE', 'INACTIVE'];
  loading: boolean = true;
  activityValues: number[] = [0, 100];
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  ngOnInit(): void {
    this.menuService.setMenu([
      'Generale',
      'Monitora statistiche',
      'Cambio password',
      'Elimina profilo',
    ]);
 this.users = [
              {
                email: 'cial@gmail.com',
                nome: 'Jonny',
                cognome: 'bravo',
                isConfirmed: true,
                id: 1,
                createdAt: '',
                isActive: true,
                deleteddAt: '',
                role: 'USER',
                companies: [this.company!],
                immagine: [{ name: '', image: '', uploadedAt: '', isCurrent: true }],
              },
              {
                email: 'cial@gmail.com',
                nome: 'Jonny',
                cognome: 'bravo',
                isConfirmed: true,
                id: 1,
                createdAt: '',
                isActive: true,
                deleteddAt: '',
                role: 'USER',
                companies: [this.company!],
                immagine: [{ name: '', image: '', uploadedAt: '', isCurrent: true }],
              },
              {
                email: 'cial@gmail.com',
                nome: 'Jonny',
                cognome: 'bravo',
                isConfirmed: true,
                id: 1,
                createdAt: '',
                isActive: true,
                deleteddAt: '',
                role: 'USER',
                companies: [this.company!],
                immagine: [{ name: '', image: '', uploadedAt: '', isCurrent: true }],
              },
            ];
    this.menuService
      .getVoice()
      .pipe(filter((v) => v != ''))
      .subscribe((data: string) => {
        this.section = data;
      });
    this.user = this.authService.getUser();
    this.company = this.authService.getCompany();
    setTimeout(() => {
      this.loading = false;
      this.cdr.detectChanges();
    }, 1000);
    if (this.company) {
      this.companyService.getUsers(this.company.id).subscribe({
        next: (data: User[]) => {
          if (data) {
            // this.users = data;
          }
        },
      });
    }
  }
  getImage(): string {
    var image: string = '';
    if (this.user) {
      image = this.user?.immagine?.filter((i) => i.isCurrent)[0]?.image;
    } else if (this.company) {
      image = this.company?.immagine?.filter((i) => i.isCurrent)[0]?.image;
      this.projectsToShow = this.company.projects;
    }
    if (!image || (image && image.length == 0)) {
      image = '/assets/logo/logo.png';
    }
    return image;
  }

  search() {
    if (this.company?.projects) {
      if (this.searchProjectForm.controls['search'].value) {
        this.projectsToShow =
          this.company?.projects.filter((p) =>
            p.name.includes(this.searchProjectForm.controls['search'].value!)
          ) || [];
      } else {
        this.projectsToShow = this.company?.projects || [];
      }
    }
  }
  clear(table: Table) {
    table.clear();
  }

  getSeverity(status: boolean) {
    switch (status) {
      case true:
        return 'success';
      case false:
        return 'danger';
      default:
        return null;
    }
  }
}
