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
import { ProjectState } from '../../../enums/enums';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { Tooltip } from 'primeng/tooltip';
import { MenuModule } from 'primeng/menu';
import { MenuItem, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';

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
    ProgressSpinnerModule,
    Tooltip,
    MenuModule,
    ButtonModule,
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
    title: new FormControl(''),
    description: new FormControl(''),
    managerId: new FormControl(''),
    createdAt: new FormControl(''),
    projectType: new FormControl(''),
  });
  protected isDark: boolean = false;
  status: string[] = ['ACTIVE', 'INACTIVE'];
  loading: boolean = true;
  protected tasks: task[] = [];
  protected chart: any;
  protected chartUsersOptions: any = null;
  protected chartProjectsOptions: any = null;
  protected chartTaskOptions: any = null;
  protected sizes: number[] = [6, 18, 48];
  protected paginationForm: FormGroup = new FormGroup({});
  protected page: number = 0;
  protected size: number = 6;
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
  protected states: ProjectState[] = [];
  protected messageService: MessageService = inject(MessageService);
  protected filtersOpened: WritableSignal<boolean> = signal(false);
  protected stateSelected: ProjectState = ProjectState.PENDING;
  protected isLoadingProjects: WritableSignal<boolean> = signal(false);
  protected isAddingProject: WritableSignal<boolean> = signal(false);
  protected sizeForm: FormGroup = new FormGroup({});
  protected showEditModal: WritableSignal<boolean> = signal(false);
  protected editProjectForm: FormGroup = new FormGroup({});
  editTitle: WritableSignal<boolean> = signal(false);
  protected items: MenuItem[] = [
    {
      label: 'Options',
      items: [
        {
          label: 'Edit',
          icon: 'pi pi-edit',
        },
        {
          label: 'Favourite',
          icon: 'pi pi-star',
        },
      ],
    },
  ];
  protected selectedItemForEdit: project | null = null;

  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.company = this.authService.getCompany();
    if (this.company) {
      this.refreshUsers();
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
    this.sizeForm = new FormGroup({
      size: new FormControl(this.size),
    });
    this.editProjectForm = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      description: new FormControl('', [Validators.required, Validators.maxLength(600)]),
      managerId: new FormControl('', Validators.required),
      typeId: new FormControl('', Validators.required),
      state: new FormControl('', Validators.required),
    });
    this.editProjectForm.valueChanges.subscribe((value: any) => {
      this.handleEditProjectChange(value);
    });
    this.loadDatas();
  }

  handleEditProjectChange(value: any) {
    this.selectedItemForEdit!.title = value.title;
    this.selectedItemForEdit!.description = value.description;
    if(this.selectedItemForEdit!.manager.id != value.managerId){
      //To do
    }
     if(this.selectedItemForEdit!.projectType.id != value.typeId){
      //To do
    }
    this.selectedItemForEdit!.projectState = value.state;
  }
  refreshUsers() {
    this.companyService.getUserList(this.company!.id).subscribe({
      next: (data: User[]) => {
        this.company!.users = data;
      },
    });
  }
  changeSize() {
    console.log(this.sizeForm.controls['size'].value);
    this.size = this.sizeForm.controls['size'].value;
    console.log(this.size);
    this.search();
  }
  chooseManager(item: User) {
    this.addProject.controls['managerId'].setValue(item.nome + ' ' + item.cognome);
    this.addProject.updateValueAndValidity();
    this.selectedManager = item;
    this.showManagers.set(false);
  }

  search(fromStates?: boolean) {
    if (fromStates) {
      this.page = 0;
    }
    this.isLoadingProjects.set(true);
    let filters = this.buildFilters();
    this.companyService.getCompanyProjects(filters, this.company!.id).subscribe({
      next: (data: Page<project>) => {
        setTimeout(() => {
          this.projectsToShow = data;
          this.isLoadingProjects.set(false);
          this.cdr.markForCheck();
        }, 500);
      },
    });
  }
  reset() {
    this.searchProjectForm.reset();
    this.stateSelected = ProjectState.PENDING;
    this.search();
  }

  previous() {
    if (this.page != 0) {
      this.page -= 1;
      this.search();
    }
  }
  next() {
    if (this.page + 1 != this.projectsToShow.totalPages) {
      this.page += 1;
      this.search();
    }
  }
  blockScroll(block: boolean) {
    if (block) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
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
  buildFilters(): CompanyProjectsFilters {
    return {
      page: this.page,
      size: this.size,
      sort: this.sort,
      order: this.order,
      projectName: this.searchProjectForm.controls['title'].value,
      state: this.stateSelected,
      description: this.searchProjectForm.controls['description'].value,
      manager: this.searchProjectForm.controls['managerId'].value,
      type: this.searchProjectForm.controls['projectType'].value,
      date: this.searchProjectForm.controls['createdAt'].value,
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
    this.states = [
      ProjectState.PENDING,
      ProjectState.STARTED,
      ProjectState.STOPPED,
      ProjectState.COMPLETED,
    ];
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
    this.isAddingProject.set(true);
    this.profileService.createProject(project).subscribe({
      next: (data: project) => {
        setTimeout(() => {
          this.isAddingProject.set(false);
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
          this.search();
          this.addProject.controls['title'].setValue('');
          this.addProject.controls['managerId'].setValue('');
          this.addProject.controls['description'].setValue('');
          this.addProject.controls['typeId'].setValue('');
          this.addProject.controls['state'].setValue('');
          this.addProjectFormSubmitted = false;
        }, 1000);
      },
    });
  }

  manageFavourite(item: project) {
    item.favourite ? this.unmarkAsFavourite(item.id) : this.markAsFavourite(item.id);
  }
  unmarkAsFavourite(id: number) {
    this.companyService.unmarkAsFavourite(id).subscribe({
      next: (data: project) => {
        this.search();
      },
    });
  }
  markAsFavourite(id: number) {
    this.companyService.markAsFavourite(id).subscribe({
      next: (data: project) => {
        this.search();
      },
    });
  }
  openEdit(item: project) {
    this.showEditModal.set(true);
    this.selectedItemForEdit = item;
    document.body.style.overflow = 'hidden';
    this.editProjectForm.patchValue({
      title: this.selectedItemForEdit.title,
      description: this.selectedItemForEdit.description,
      managerId: this.selectedItemForEdit.manager.id,
      typeId: this.selectedItemForEdit.projectType.id,
      state: this.selectedItemForEdit.projectState,
    });
  }
  closeEdit() {
    this.showEditModal.set(false);
    this.selectedItemForEdit = null;
    document.body.style.overflow = '';
  }
  applyFilters() {}
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
      this.cdr.markForCheck();
    });
  }
}
