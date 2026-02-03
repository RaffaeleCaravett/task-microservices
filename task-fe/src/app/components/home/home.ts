import { NgClass } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  effect,
  ElementRef,
  HostListener,
  inject,
  OnInit,
  viewChild,
} from '@angular/core';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { ModeService } from '../../services/mode.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [NgClass, ReactiveFormsModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class HomeComponent implements OnInit, AfterViewInit {
  isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected router: Router = inject(Router);
  homeForm: FormGroup = new FormGroup({});
  protected homeCards: any[] = [];
  canvas = viewChild<HTMLCanvasElement>('canvas');
  ngOnInit(): void {
    this.onResize(null);
    this.initForm();
    this.initCards();
  }
  ngAfterViewInit(): void {
    const canvasEl = this.canvas();
    if (!canvasEl) return;

    console.log(canvasEl); // HTMLCanv
  }

  initForm() {
    this.homeForm = new FormGroup({
      email: new FormControl('', [
        Validators.required,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/),
      ]),
    });
  }
  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    // let home = document.getElementsByClassName('home')[0] as HTMLDivElement;
    // if (home != null) {
    //   home.style.minHeight = window.innerHeight - (window.innerHeight * 25) / 100 + 'px';
    // }
  }
  checkPermission() {
    if (this.homeForm.valid) {
      this.router.navigate(['signup', this.homeForm.controls['email'].value]);
    }
  }

  initCards() {
    this.homeCards = [
      {
        id: 1,
        title: 'Track',
        description: 'Track everything you want: progresses, tasks, activities and more.',
        icon: 'pi pi-chart-line',
      },
      {
        id: 2,
        title: 'Control',
        description: 'Take control the things that you want. Don\t lose abit of anything.',
        icon: 'pi pi-check-circle',
      },
      {
        id: 3,
        title: 'Delegate',
        description:
          'Assign tasks and more to the people you trust or with the ones you want to share things with.',
        icon: 'pi pi-user-plus',
      },
      {
        id: 4,
        title: 'Organize',
        description: 'Create your own organization. Build your dream team.',
        icon: 'pi pi-sitemap',
      },
    ];
  }
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
      this.cdr.markForCheck();
    });
  }
}
