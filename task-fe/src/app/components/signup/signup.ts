import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule, TooltipModule],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class SignupComponent implements OnInit {
  router: Router = inject(Router);
  signupForm: FormGroup = new FormGroup({});

  ngOnInit(): void {
    this.signupForm = new FormGroup({
      ragioneSociale: new FormControl('', Validators.required),
      partitaIva: new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]),
      formaGiuridica: new FormControl('', Validators.required),
      paeseDiRegistrazione: new FormControl('', Validators.required),
      indirizzo: new FormGroup({
        citta: new FormControl('', Validators.required),
        cap: new FormControl('', Validators.required),
        regione: new FormControl('', Validators.required),
        via: new FormControl('', Validators.required),
      }),
      settore: new FormControl('', Validators.required),
      dimensioniAzienda: new FormControl('', Validators.required),
      differentWorkStation: new FormControl(0),
      sedeOperativa: new FormGroup({
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
}
