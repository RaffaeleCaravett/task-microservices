import { ChangeDetectorRef, Component, effect, inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { TooltipModule } from 'primeng/tooltip';
import { login, loginSuccess } from '../../interfaces/interfaces';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { InputOtpModule } from 'primeng/inputotp';
import { NgClass, NgStyle } from '@angular/common';
import { ModeService } from '../../services/mode.service';
import { ButtonModule } from 'primeng/button';
import { debug } from 'three/src/nodes/TSL.js';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    InputGroupModule,
    InputGroupAddonModule,
    TooltipModule,
    InputOtpModule,
    FormsModule,
    NgClass,
    ButtonModule,
    NgStyle,
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup = new FormGroup({});
  router: Router = inject(Router);
  toastr: ToastrService = inject(ToastrService);
  mode: ModeService = inject(ModeService);
  protected types: string[] = ['user', 'company'];
  protected authService: AuthService = inject(AuthService);
  protected section: string = 'login';
  protected isLoginLoading: boolean = false;
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected value: string = '';
  protected type: string = '';
  protected showTypes: boolean = false;
  protected isDark: boolean = false;
  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required]),
      password: new FormControl('', [
        Validators.required,
        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/,
        ),
      ]),
      accessCode: new FormControl(''),
    });
  }
  login() {
    if (this.loginForm.valid) {
      let body: login = {
        email: this.loginForm.controls['email'].value,
        password: this.loginForm.controls['password'].value,
      };
      let accessCode: string | null = null;
      if (this.loginForm.controls['accessCode'].value) {
        accessCode = this.loginForm.controls['accessCode'].value;
      }
      this.authService.login(body, this.type, accessCode ? accessCode : null).subscribe({
        next: (data: boolean | loginSuccess) => {
          if (data) {
            this.isLoginLoading = true;
            this.cdr.markForCheck();
            setTimeout(() => {
              if (this.type == 'company') {
                this.section = 'access-code';
                this.isLoginLoading = false;
              } else {
                this.verifyCode(null, data as loginSuccess)
              }
            }, 2000);
          }
        },
      });
    } else {
      this.toastr.error('Completa correttamente il form prima');
    }
  }
  goToSignup() {
    this.router.navigate(['/signup/page']);
  }
  verifyCode(code?: string | null, data?: loginSuccess) {
    if (code) {
      this.authService
        .verifyCode(this.loginForm.controls['email'].value, code, this.type.toUpperCase())
        .subscribe({
          next: (data: loginSuccess) => {
            if (data && data.token.accessToken) {
              localStorage.setItem('accessToken', data.token.accessToken);
              localStorage.setItem('refreshToken', data.token.refreshToken);
              setTimeout(() => {
                this.authService.checkToken();
                this.router.navigate(['/dashboard']);
                this.cdr.detectChanges();
              }, 1000);
            }
          },
        });
    } else {
      debugger
      if (data && data.token.accessToken) {
        localStorage.setItem('accessToken', data.token.accessToken);
        localStorage.setItem('refreshToken', data.token.refreshToken);
        setTimeout(() => {
          this.authService.checkToken();
          this.router.navigate(['/dashboard']);
          this.cdr.detectChanges();
        }, 1000);
      }
    }
  }

  showTypesSwitch() {
    this.showTypes = !this.showTypes;
    if (!this.showTypes) {
      this.type = '';
    }
    if (this.type == 'user') {
      this.loginForm.controls['accessCode'].setValidators(Validators.required);
      this.loginForm.updateValueAndValidity();
    } else {
      this.loginForm.controls['accessCode'].clearValidators();
      this.loginForm.updateValueAndValidity();
    }
  }
  constructor() {
    effect(() => {
      this.isDark = this.mode.isDark();
    });
  }
}
