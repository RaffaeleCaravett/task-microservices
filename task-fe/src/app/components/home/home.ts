import { NgClass } from '@angular/common';
import { ChangeDetectorRef, Component, effect, HostListener, inject, OnInit } from '@angular/core';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { ModeService } from '../../services/mode.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [NgClass, ReactiveFormsModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class HomeComponent implements OnInit {
  isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected router: Router = inject(Router);
  homeForm: FormGroup = new FormGroup({});
  ngOnInit(): void {
    this.onResize(null);
    this.initForm();
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
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
      this.cdr.markForCheck();
    });
  }
}
