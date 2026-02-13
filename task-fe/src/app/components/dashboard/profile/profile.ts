import {
  afterNextRender,
  ChangeDetectorRef,
  Component,
  effect,
  inject,
  OnInit,
} from '@angular/core';
import { MenuService } from '../../../services/menu.service';
import { filter, take } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import {
  Company,
  CompanyProjectsFilters,
  Page,
  project,
  task,
  User,
} from '../../../interfaces/interfaces';
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
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { ProjectState, TaskState } from '../../../enums/enums';
import { ProfileService } from '../../../services/profile.service';
import { ModeService } from '../../../services/mode.service';
import { RouterLink } from '@angular/router';

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
    CanvasJSAngularChartsModule,
    NgClass,
    RouterLink,
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class ProfileComponent implements OnInit {
  protected menuService: MenuService = inject(MenuService);
  protected companyService: CompanyService = inject(CompanyService);
  protected authService: AuthService = inject(AuthService);
  private modeService: ModeService = inject(ModeService);
  private cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected profileService: ProfileService = inject(ProfileService);

  protected section: string = 'Generale';
  protected user: User | null = null;
  protected company: Company | null = null;
  protected users!: Page<User>;
  protected projectsToShow: project[] = [];
  protected searchProjectForm = new FormGroup({
    search: new FormControl(''),
  });
  protected isDark: boolean = false;
  status: string[] = ['ACTIVE', 'INACTIVE'];
  loading: boolean = true;
  protected tasks: task[] = [];
  protected chart: any;
  protected chartUsersOptions: any = null;
  protected chartProjectsOptions: any = null;
  protected chartTaskOptions: any = null;
  protected sizes: number[] = [10, 20, 50];
  protected paginationForm: FormGroup = new FormGroup({});
  protected page: number = 0;
  protected size: number = 20;
  protected sort: string = 'id';
  protected order: string = 'asc';
  protected usersForCount: number = 0;
  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.company = this.authService.getCompany();
    this.search();
    setTimeout(() => {
      this.loading = false;
      this.cdr.detectChanges();
    }, 1000);
  }

  search() {
    const projectName = this.searchProjectForm.controls['search'].value;
      let filters = this.buildFilters(projectName);
      this.companyService.getCompanyProjects(filters, this.company!.id).subscribe({
        next: (data: any) => {},
      });
  }

  buildFilters(projectName: string | null): CompanyProjectsFilters {
    return {
      page: this.page,
      size: this.size,
      sort: this.sort,
      order: this.order,
      projectName: projectName,
    };
  }

  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
      this.cdr.markForCheck();
    });
  }
}
