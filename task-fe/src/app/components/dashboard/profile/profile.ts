import {
  afterNextRender,
  ChangeDetectorRef,
  Component,
  effect,
  inject,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { MenuService } from '../../../services/menu.service';
import { debounceTime, filter, take } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import {
  Company,
  CompanyProjectsFilters,
  Page,
  project,
  projectDTO,
  projectType,
  task,
  User,
} from '../../../interfaces/interfaces';
import { DatePipe, NgClass } from '@angular/common';
import { CompanyService } from '../../../services/company.service';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { TagModule } from 'primeng/tag';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { MultiSelectModule } from 'primeng/multiselect';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { ProfileService } from '../../../services/profile.service';
import { ModeService } from '../../../services/mode.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-profile',
  imports: [
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

  protected user: User | null = null;
  protected company: Company | null = null;
  protected users!: Page<User>;
  protected projectsToShow!: Page<project>;
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
  protected pageable: any;
  protected usersForCount: number = 0;
  protected addMode: WritableSignal<boolean> = signal(false);
  protected addProject: FormGroup = new FormGroup({});
  protected addProjectFormSubmitted: boolean = false;
  protected selectedManager: User | null = null;
  showManagers: WritableSignal<boolean> = signal(false);
  protected filteredUsers: User[] = [];
  protected types: projectType[] = [];
  protected states: string[] = [];
  protected messageService: MessageService = inject(MessageService);
  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.company = this.authService.getCompany();
    if (this.company) {
      this.filteredUsers = [...this.company.users];
      this.search();
    }
    setTimeout(() => {
      this.loading = false;
      this.cdr.detectChanges();
    }, 1000);

    this.searchProjectForm.valueChanges.pipe(debounceTime(300)).subscribe((data) => {});
    this.addProject = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      description: new FormControl('', [Validators.required, Validators.maxLength(600)]),
      managerId: new FormControl('', Validators.required),
      typeId: new FormControl('', Validators.required),
      state: new FormControl('', Validators.required),
    });
    this.addProject.valueChanges.pipe(debounceTime(300)).subscribe((data) => {
      this.filterManagers(data.managerId);
    });
    this.loadDatas();
  }
  chooseManager(item: User) {
    this.addProject.controls['managerId'].setValue(item.nome + ' ' + item.cognome);
    this.addProject.updateValueAndValidity();
    this.selectedManager = item;
    this.showManagers.set(false);
  }

  search() {
    const projectName = this.searchProjectForm.controls['search'].value;
    let filters = this.buildFilters(projectName);
    this.companyService.getCompanyProjects(filters, this.company!.id).subscribe({
      next: (data: Page<project>) => {
        this.projectsToShow = data;
      },
    });
  }

  filterManagers(value: string | null | undefined) {
    if (value) {
      let filtered = this.company?.users.filter(
        (u: User) =>
          u.nome.toLowerCase().includes(value.toLowerCase()) ||
          u.cognome.toLowerCase().includes(value.toLowerCase()),
      );
      this.filteredUsers = filtered || [];
    } else {
      this.filteredUsers = this.company?.users || [];
    }
    this.cdr.markForCheck();
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

  switchToAddMode() {
    this.addMode.set(true);
  }

  loadDatas() {
    this.profileService.getProjectTypes().subscribe({
      next: (data: projectType[]) => {
        this.types = data;
      },
    });
    this.states = ['PENDING', 'STARTED', 'STOPPED', 'COMPLETED'];
  }

  createProject() {
    this.addProjectFormSubmitted = true;
    if (this.addProject.invalid || !this.selectedManager) {
      return;
    }
    let project: projectDTO = {
      title: this.addProject.controls['title'].value,
      description: this.addProject.controls['description'].value,
      managerId: this.selectedManager.id,
      typeId: this.addProject.controls['typeId'].value,
      state: this.addProject.controls['state'].value,
      companyId: this.company?.id || 0,
    };
    this.profileService.createProject(project).subscribe({
      next: (data: project) => {
        if (data && data.id) {
          this.messageService.add({
            severity: 'success',
            summary: 'Project added',
            detail: 'Congratulations, you\ve succesfuly added your project. Go work for it!',
            life: 3000,
          });
        }
        this.addMode.set(false);
        this.loadDatas();
      },
    });
  }
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
      this.cdr.markForCheck();
    });
  }
}
