import { Component, inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { TooltipModule } from 'primeng/tooltip';
import { AuthService } from '../../services/auth.service';
import { formaGiuridica, nazione, settore } from '../../interfaces/interfaces';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule, TooltipModule],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup = new FormGroup({});
  router: Router = inject(Router);
  authService: AuthService = inject(AuthService);
  formBuilder: FormBuilder = inject(FormBuilder);
  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      ragioneSociale: new FormControl('', Validators.required),
      partitaIva: new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]),
      formaGiuridica: new FormControl('', Validators.required),
      indirizzo: this.formBuilder.group({
        nazione: new FormControl('', Validators.required),
        citta: new FormControl('', Validators.required),
        cap: new FormControl('', Validators.required),
        regione: new FormControl('', Validators.required),
        via: new FormControl('', Validators.required),
      }),
      settore: new FormControl('', Validators.required),
      dimensioniAzienda: new FormControl('', Validators.required),
      differentWorkStation: new FormControl(0),
      sedeOperativa: this.formBuilder.group({
        nazione: new FormControl('', Validators.required),
        citta: new FormControl(''),
        cap: new FormControl(''),
        regione: new FormControl(''),
        via: new FormControl(''),
      }),
      email: new FormControl('', [Validators.required, Validators.pattern(/£/)]),
      password: new FormControl('', [
        Validators.required,
        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/
        ),
      ]),
      ripetiPassword: new FormControl('', [
        Validators.required,
        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/
        ),
      ]),
    });
    this.getDatas();
  }
  signup() {}

  passwordMismatch(): boolean {
    return (
      this.signupForm.controls['password'].value != this.signupForm.controls['ripetiPassword'].value
    );
  }
  goToLogin() {
    this.router.navigate(['home/login']);
  }
  getDatas() {
    this.authService.getNazioni().subscribe({
      next: (nazioni: nazione[]) => {
        console.log(nazioni);
      },
    });
    this.authService.getSettori().subscribe({
      next: (nazioni: settore[]) => {
        console.log(nazioni);
      },
    });
    this.authService.getForme().subscribe({
      next: (nazioni: formaGiuridica[]) => {
        console.log(nazioni);
      },
    });
  }
}
