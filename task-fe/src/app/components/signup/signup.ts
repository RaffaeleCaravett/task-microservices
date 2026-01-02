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
import {
  cap,
  citta,
  dimensioni,
  formaGiuridica,
  nazione,
  piano,
  regione,
  settore,
} from '../../interfaces/interfaces';
import { CurrencyPipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule, TooltipModule, CurrencyPipe, NgClass],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup = new FormGroup({});
  router: Router = inject(Router);
  authService: AuthService = inject(AuthService);
  formBuilder: FormBuilder = inject(FormBuilder);
  regioni: regione[] = [];
  nazioni: nazione[] = [];
  citta: citta[] = [];
  cap: cap[] = [];
  regioniSede: regione[] = [];
  nazioniSede: nazione[] = [];
  cittaSede: citta[] = [];
  capSede: cap[] = [];
  settori: settore[] = [];
  forme: formaGiuridica[] = [];
  dimensioni: dimensioni[] = [];
  step: number = 1;
  piani: piano[] = [];
  choosedPlan: piano | null = null;
  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      ragioneSociale: new FormControl('', Validators.required),
      partitaIva: new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]),
      formaGiuridica: new FormControl('', Validators.required),
      indirizzo: this.formBuilder.group({
        nazione: new FormControl(
          { value: '', disabled: this.nazioni.length == 0 },
          Validators.required
        ),
        citta: new FormControl(
          { value: '', disabled: this.citta.length == 0 },
          Validators.required
        ),
        cap: new FormControl({ value: '', disabled: this.cap.length == 0 }, Validators.required),
        regione: new FormControl(
          { value: '', disabled: this.regioni.length == 0 },
          Validators.required
        ),
        via: new FormControl('', Validators.required),
      }),
      settore: new FormControl('', Validators.required),
      dimensioniAzienda: new FormControl('', Validators.required),
      differentWorkStation: new FormControl(0),
      sedeOperativa: this.formBuilder.group({
        nazione: new FormControl({ value: '', disabled: this.nazioniSede.length == 0 }),
        citta: new FormControl({ value: '', disabled: this.cittaSede.length == 0 }),
        cap: new FormControl({ value: '', disabled: this.capSede.length == 0 }),
        regione: new FormControl({ value: '', disabled: this.regioniSede.length == 0 }),
        via: new FormControl(''),
      }),
      email: new FormControl('', [
        Validators.required,
        Validators.pattern(/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/),
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/),
      ]),
      ripetiPassword: new FormControl('', [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/),
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
        this.nazioni = nazioni;
        this.nazioniSede = nazioni;
        this.signupForm.controls['sedeOperativa'].get('nazione')?.enable();
        this.signupForm.controls['indirizzo'].get('nazione')?.enable();
        this.signupForm.updateValueAndValidity();
      },
    });
    this.authService.getSettori().subscribe({
      next: (settori: settore[]) => {
        this.settori = settori;
      },
    });
    this.authService.getForme().subscribe({
      next: (forme: formaGiuridica[]) => {
        this.forme = forme;
      },
    });
    this.authService.getDimensioni().subscribe({
      next: (dimensioni: dimensioni[]) => {
        this.dimensioni = dimensioni;
      },
    });
    this.authService.getPiani().subscribe({
      next: (datas: piano[]) => {
        this.piani = datas;
      },
    });
  }

  onChangeNations(id: number | null | undefined, type: string) {
    if (type == 'indirizzo') {
      this.regioni = [];
      this.citta = [];
      this.cap = [];
      this.signupForm.controls['indirizzo'].get('regione')?.setValue(null);
      this.signupForm.controls['indirizzo'].get('citta')?.setValue(null);
      this.signupForm.controls['indirizzo'].get('cap')?.setValue(null);
      this.signupForm.controls['indirizzo'].get('regione')?.disable();
      this.signupForm.controls['indirizzo'].get('citta')?.disable();
      this.signupForm.controls['indirizzo'].get('cap')?.disable();
    } else {
      this.regioniSede = [];
      this.cittaSede = [];
      this.capSede = [];
      this.signupForm.controls['sedeOperativa'].get('regione')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('citta')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('cap')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('regione')?.disable();
      this.signupForm.controls['sedeOperativa'].get('citta')?.disable();
      this.signupForm.controls['sedeOperativa'].get('cap')?.disable();
    }
    this.signupForm.updateValueAndValidity();
    if (id && id != undefined) {
      this.authService.getRegioni(id).subscribe({
        next: (datas: regione[]) => {
          if (type == 'indirizzo') {
            this.regioni = datas;
            this.signupForm.controls['indirizzo'].get('regione')?.enable();
          } else {
            this.regioniSede = datas;
            this.signupForm.controls['sedeOperativa'].get('regione')?.enable();
          }
          this.signupForm.updateValueAndValidity();
        },
      });
    }
  }

  onChangeRegions(id: number | null | undefined, type: string) {
    if (type == 'indirizzo') {
      this.citta = [];
      this.cap = [];
      this.signupForm.controls['indirizzo'].get('citta')?.setValue(null);
      this.signupForm.controls['indirizzo'].get('cap')?.setValue(null);
      this.signupForm.controls['indirizzo'].get('citta')?.disable();
      this.signupForm.controls['indirizzo'].get('cap')?.disable();
    } else {
      this.cittaSede = [];
      this.capSede = [];
      this.signupForm.controls['sedeOperativa'].get('citta')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('cap')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('citta')?.disable();
      this.signupForm.controls['sedeOperativa'].get('cap')?.disable();
    }
    this.signupForm.updateValueAndValidity();
    if (id && id != undefined) {
      this.authService.getCitta(id).subscribe({
        next: (datas: citta[]) => {
          if (type == 'indirizzo') {
            this.citta = datas;
            this.signupForm.controls['indirizzo'].get('citta')?.enable();
          } else {
            this.cittaSede = datas;
            this.signupForm.controls['sedeOperativa'].get('citta')?.enable();
          }
          this.signupForm.updateValueAndValidity();
        },
      });
    }
  }
  onChangeCitta(id: number | null | undefined, type: string) {
    if (type == 'indirizzo') {
      this.cap = [];
      this.signupForm.controls['indirizzo'].get('cap')?.disable();
    } else {
      this.capSede = [];
      this.signupForm.controls['sedeOperativa'].get('cap')?.disable();
    }
    this.signupForm.updateValueAndValidity();
    if (id && id != undefined) {
      this.authService.getCap(id).subscribe({
        next: (datas: cap[]) => {
          if (type == 'indirizzo') {
            this.cap = datas;
            this.signupForm.controls['indirizzo'].get('cap')?.enable();
          } else {
            this.capSede = datas;
            this.signupForm.controls['sedeOperativa'].get('cap')?.enable();
          }

          this.signupForm.updateValueAndValidity();
        },
      });
    }
  }
  updateSedeOperativa() {
    if (this.signupForm.controls['differentWorkStation'].value == true) {
      this.signupForm.controls['sedeOperativa'].get('nazione')?.setValidators(Validators.required);
      this.signupForm.controls['sedeOperativa'].get('citta')?.setValidators(Validators.required);
      this.signupForm.controls['sedeOperativa'].get('cap')?.setValidators(Validators.required);
      this.signupForm.controls['sedeOperativa'].get('regione')?.setValidators(Validators.required);
      this.signupForm.controls['sedeOperativa'].get('via')?.setValidators(Validators.required);
    } else {
      this.signupForm.controls['sedeOperativa'].get('nazione')?.clearValidators();
      this.signupForm.controls['sedeOperativa'].get('citta')?.clearValidators();
      this.signupForm.controls['sedeOperativa'].get('cap')?.clearValidators();
      this.signupForm.controls['sedeOperativa'].get('regione')?.clearValidators();
      this.signupForm.controls['sedeOperativa'].get('via')?.clearValidators();
      this.signupForm.controls['sedeOperativa'].get('nazione')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('citta')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('cap')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('regione')?.setValue(null);
      this.signupForm.controls['sedeOperativa'].get('via')?.setValue(null);
    }
    this.signupForm.updateValueAndValidity();
  }
}
