import { afterNextRender, ChangeDetectorRef, Component, effect, inject, OnInit } from '@angular/core';
import { MenuService } from '../../../services/menu.service';
import { filter, take } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { Company, Page, project, task, User } from '../../../interfaces/interfaces';
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
  protected searchUserForm: FormGroup = new FormGroup({});
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
  protected size: number = 10;
  protected usersForCount: number = 0;
  ngOnInit(): void {
    this.searchUserForm = new FormGroup({
      email: new FormControl(),
      fullname: new FormControl(),
      status: new FormControl('ACTIVE'),
    });
    this.paginationForm = new FormGroup({
      page: new FormControl(null),
      size: new FormControl(null),
    });
    this.menuService.setMenu([
      'Generale',
      'Monitora statistiche',
      'Cambia informazioni generali del profilo',
      'Cambio password',
      'Elimina profilo',
    ]);
    this.chartUsersOptions = {
      title: {
        text: 'Added team members overtime (x: number, y: month)',
      },
      animationEnabled: true,
      data: [
        {
          type: 'column',
          dataPoints: [
            {
              x: 1,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '01')
                ?.length,
            },
            {
              x: 2,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '02')
                ?.length,
            },
            {
              x: 3,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '03')
                ?.length,
            },
            {
              x: 4,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '04')
                ?.length,
            },
            {
              x: 5,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '05')
                ?.length,
            },
            {
              x: 6,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '06')
                ?.length,
            },
            {
              x: 7,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '07')
                ?.length,
            },
            {
              x: 8,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '08')
                ?.length,
            },
            {
              x: 9,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '09')
                ?.length,
            },
            {
              x: 10,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '10')
                ?.length,
            },
            {
              x: 11,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '11')
                ?.length,
            },
            {
              x: 12,
              y: this.users?.content?.filter((u: User) => u.createdAt.split('-')[1] == '12')
                ?.length,
            },
          ],
        },
      ],
    };
    this.menuService
      .getVoice()
      .pipe(filter((v) => v != ''))
      .subscribe((data: string) => {
        this.section = data;
      });
    this.user = this.authService.getUser();
    this.company = this.authService.getCompany();
    if (this.company) {
      this.chartProjectsOptions = {
        title: {
          text: 'Projects',
        },
        animationEnabled: true,
        axisY: {
          includeZero: true,
          suffix: '',
        },
        data: [
          {
            type: 'bar',
            indexLabel: '{y}',
            yValueFormatString: '#,###',
            dataPoints: [
              {
                label: 'Actives',
                y:
                  this.company?.projects?.filter((p) => p.state == ProjectState.STARTED)?.length ||
                  0,
              },
              {
                label: 'Completed',
                y:
                  this.company?.projects?.filter((p) => p.state == ProjectState.COMPLETED)
                    ?.length || 0,
              },
            ],
          },
        ],
      };
      this.company?.projects
        ?.map((p) => p.task)
        ?.forEach((p) => p?.forEach((t) => this.tasks?.push(t)));
      this.chartTaskOptions = {
        animationEnabled: true,
        title: {
          text: 'Tasks statistics year over year',
        },
        axisX: {
          tickThickness: 0,
          interval: 1,
          intervalType: 'month',
        },
        toolTip: {
          shared: true,
        },
        axisY: {
          lineThickness: 0,
          tickThickness: 0,
          interval: 30,
        },
        legend: {
          verticalAlign: 'center',
          horizontalAlign: 'right',
          reversed: true,
        },
        data: [
          {
            name: 'Created',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#004B8D ',
            dataPoints: [
              {
                x: new Date(2025, 0),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '01' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 0),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '01' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 0),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '01' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Created',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 1),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '02' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 1),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '02' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 1),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '02' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 2),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '03' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 2),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '03' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 3),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '04' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 3),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '04' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 3),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '04' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 4),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '05' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 4),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '05' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 4),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '05' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 5),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '06' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 5),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '06' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 5),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '06' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 6),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '07' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 6),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '07' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 6),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '07' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 7),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '08' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 7),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '08' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 7),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '08' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 8),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '09' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 8),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '09' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 8),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '09' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 9),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '10' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 9),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '10' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 9),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '10' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 10),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '11' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 10),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '11' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 10),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '11' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
              {
                x: new Date(2025, 11),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '12' &&
                      t.state == TaskState.CREATED,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'On going',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#466e91ff ',
            dataPoints: [
              {
                x: new Date(2025, 11),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '12' &&
                      t.state == TaskState.ON_GOING,
                  )?.length || 3,
              },
            ],
          },
          {
            name: 'Completed',
            showInLegend: true,
            type: 'stackedColumn100',
            color: '#6e7e8dff ',
            dataPoints: [
              {
                x: new Date(2025, 11),
                y:
                  this.tasks?.filter(
                    (t) =>
                      t.createdAt.split('-')[0] == '2025' &&
                      t.createdAt.split('-')[1] == '12' &&
                      t.state == TaskState.COMPLETED,
                  )?.length || 3,
              },
            ],
          },
        ],
      };
      this.companyService.getUsers(this.company.id).subscribe({
        next: (data: Page<User>) => {
          if (data) {
            this.usersForCount = data?.content?.length || 0;
          }
        },
      });
    }
    setTimeout(() => {
      this.loading = false;
      this.cdr.detectChanges();
    }, 1000);
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
            p.name.includes(this.searchProjectForm.controls['search'].value!),
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
  getInitials(user: User) {
    return user.nome.substring(0, 1).toUpperCase() + user.cognome.substring(0, 1).toUpperCase();
  }

  check() {
    let email: string = this.searchUserForm.controls['email'].value;
    let fullname: string = this.searchUserForm.controls['fullname'].value;
    let status: string = this.searchUserForm.controls['status'].value;
    this.profileService
      .findUsersByCompanyAndFilters(
        this.company!.id,
        {
          email: email || '',
          fullname: fullname || '',
          status: status || '',
        },
        this.page,
        this.size,
      )
      .pipe(take(1))
      .subscribe({
        next: (data: any) => {
          this.users = data;
          this.cdr.detectChanges();
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
