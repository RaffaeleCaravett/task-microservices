import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
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

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    InputGroupModule,
    InputGroupAddonModule,
    TooltipModule,
    InputOtpModule,
    FormsModule,
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup = new FormGroup({});
  router: Router = inject(Router);
  toastr: ToastrService = inject(ToastrService);
  protected types: string[] = ['user', 'company'];
  protected authService: AuthService = inject(AuthService);
  protected section: string = 'login';
  protected isLoginLoading: boolean = false;
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected value: string = '';
  protected type: string = 'user';
  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required]),
      password: new FormControl('', [
        Validators.required,
        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/
        ),
      ]),
      type: new FormControl('user', Validators.required),
    });
  }
  login() {
    if (this.loginForm.valid) {
      let body: login = {
        email: this.loginForm.controls['email'].value,
        password: this.loginForm.controls['password'].value,
      };
      this.authService.login(body, this.loginForm.controls['type'].value).subscribe({
        next: (data: boolean) => {
          if (data) {
            this.isLoginLoading = true;
            setTimeout(() => {
              this.section = 'access-code';
              this.isLoginLoading = false;
              this.type = this.loginForm.controls['type'].value;
              setTimeout(() => {
                let sel = document.getElementById('type');
                (sel as HTMLSelectElement).value = this.type;
                this.cdr.markForCheck();
              }, 500);
              this.cdr.markForCheck();
            }, 2000);
          }
        },
      });
    } else {
      this.toastr.error('Completa correttamente il form prima');
    }
  }
  goToSignup() {
    this.router.navigate(['home/signup']);
  }
  verifyCode(code: string) {
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
  }
}
