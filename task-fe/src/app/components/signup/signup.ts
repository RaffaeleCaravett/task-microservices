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
  CompanyDTOFromSignup,
  CompanySignup,
  dimensioni,
  formaGiuridica,
  loginSuccess,
  metodoPagamento,
  nazione,
  piano,
  regione,
  settore,
  token,
} from '../../interfaces/interfaces';
import { CurrencyPipe, NgClass } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { InputOtp } from 'primeng/inputotp';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule, TooltipModule, CurrencyPipe, NgClass, FormsModule, InputOtp],
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
  paymentForm: FormGroup = new FormGroup({});
  chosedDays: string | null = null;
  protected currentYear: number = 0;
  protected paymentAccepted: boolean = false;
  protected toastr: ToastrService = inject(ToastrService);
  protected paymentIsLoading: boolean = false;
  protected paymentMethod: metodoPagamento | null = null;
  protected sbDays: number[] = [30, 60, 160, 365];
  protected section: string = 'signup';
  protected showSpinner: boolean = false;
  protected value: string = '';
  protected registredCompany: CompanyDTOFromSignup | null = null;
  ngOnInit(): void {
    this.currentYear = Number(new Date().getFullYear().toString().substring(2, 4));
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
    this.paymentForm = new FormGroup({
      cardNumber: new FormControl('', [
        Validators.required,
        Validators.minLength(19),
        Validators.maxLength(19),
        Validators.pattern(/^\d{4}( \d{4}){3}$/),
      ]),
      expirationMonth: new FormControl('', [
        Validators.required,
        Validators.max(12),
        Validators.maxLength(2),
        Validators.min(Number(0o1)),
      ]),
      expirationYear: new FormControl('', [
        Validators.required,
        Validators.max(99),
        Validators.maxLength(2),
        Validators.min(Number(this.currentYear)),
      ]),
      securityCode: new FormControl('', [
        Validators.required,
        Validators.min(111),
        Validators.max(999),
      ]),
      cardHolder: new FormControl('', [Validators.required, Validators.pattern('^.+\\s.+$')]),
    });
    this.getDatas();
  }
  signup() {
    if (
      this.signupForm.valid &&
      !this.passwordMismatch() &&
      this.paymentMethod &&
      this.choosedPlan &&
      ((!this.chosedDays && this.choosedPlan.id == 1) ||
        (this.chosedDays && this.choosedPlan.id != 1))
    ) {
      let companySignupDTO: CompanySignup = this.buildCompanySignup();

      this.authService.signup(companySignupDTO).subscribe({
        next: (datas: CompanyDTOFromSignup) => {
          this.showSpinner = true;
          setTimeout(() => {
            this.section = 'access-code';
            this.showSpinner = false;
            this.registredCompany = datas;
          }, 3000);
        },
      });
    }
  }
  verifyCode(code: string) {
    this.authService.verifyCode(this.registredCompany!.id, code).subscribe({
      next: (data: loginSuccess) => {
        if (data && data.token.accessToken) {
          localStorage.setItem('accessToken', data.token.accessToken);
          localStorage.setItem('refreshToken', data.token.refreshToken);
          this.authService.setAccessToken(data.token.accessToken);
          this.authService.setRefreshToken(data.token.refreshToken);
          this.authService.setIsLoggedIn(true);
          if (data.company) {
            this.authService.setCompany(data.company!);
          } else if (data.user) {
            this.authService.setUser(data.user);
          }
          this.router.navigate(['/dashboard']);
        }
      },
    });
  }
  buildCompanySignup(): CompanySignup {
    return {
      ragioneSociale: this.signupForm.controls['ragioneSociale'].value,
      partitaIva: this.signupForm.controls['partitaIva'].value,
      formaGiuridica: this.signupForm.controls['formaGiuridica'].value,
      nazione: this.signupForm.controls['indirizzo'].get('nazione')?.value,
      citta: this.signupForm.controls['indirizzo'].get('citta')?.value,
      cap: this.signupForm.controls['indirizzo'].get('cap')?.value,
      regione: this.signupForm.controls['indirizzo'].get('regione')?.value,
      via: this.signupForm.controls['indirizzo'].get('via')?.value,
      settore: this.signupForm.controls['settore'].value,
      dimensioniAzienda: this.signupForm.controls['dimensioniAzienda'].value,
      nazioneSede: this.signupForm.controls['sedeOperativa'].get('nazione')?.value,
      cittaSede: this.signupForm.controls['sedeOperativa'].get('citta')?.value,
      capSede: this.signupForm.controls['sedeOperativa'].get('cap')?.value,
      regioneSede: this.signupForm.controls['sedeOperativa'].get('regione')?.value,
      viaSede: this.signupForm.controls['sedeOperativa'].get('via')?.value,
      pianoId: this.choosedPlan!.id,
      subscriptionDays: Number(this.chosedDays),
      metodoPagamentoDTO: this.paymentMethod!,
    };
  }
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

  getNation(id: number, isSede?: boolean) {
    if (isSede) return this.nazioniSede.filter((n) => (n.id = id))[0].name;
    return this.nazioni.filter((n) => (n.id = id))[0].name;
  }
  getRegion(id: number, isSede?: boolean) {
    if (isSede) return this.regioniSede.filter((n) => (n.id = id))[0].name;
    return this.regioni.filter((n) => (n.id = id))[0].name;
  }
  getCitta(id: number, isSede?: boolean) {
    if (isSede) return this.cittaSede.filter((n) => (n.id = id))[0].name;
    return this.citta.filter((n) => (n.id = id))[0].name;
  }
  getCap(id: number, isSede?: boolean) {
    if (isSede) return this.capSede.filter((n) => (n.id = id))[0].name;
    return this.cap.filter((n) => (n.id = id))[0].name;
  }
  getSettore(id: number) {
    return this.settori.filter((n) => (n.id = id))[0].name;
  }
  getForma(id: number) {
    return this.forme.filter((n) => (n.id = id))[0].name;
  }
  getDimensione(id: number) {
    return this.dimensioni.filter((n) => (n.id = id))[0].label;
  }
  pay() {
    this.paymentIsLoading = true;
    setTimeout(() => {
      this.paymentIsLoading = false;
      if (this.paymentForm.valid) {
        let currentMonth = new Date().getMonth();
        if (
          currentMonth + 1 >= this.paymentForm.controls['expirationMonth'].value &&
          this.currentYear == this.paymentForm.controls['expirationYear'].value
        ) {
          this.toastr.error("We can't add this payment method: expiration too near.");
        } else {
          this.toastr.success(
            "We've succesfuly checked your card, and added as your payment method."
          );
          this.paymentAccepted = true;
          this.paymentMethod = {
            cardNumber: this.paymentForm.controls['cardNumber'].value as string,
            month: this.paymentForm.controls['expirationMonth'].value as number,
            year: this.paymentForm.controls['expirationYear'].value as number,
            secretCode: this.paymentForm.controls['securityCode'].value as number,
            owner: this.paymentForm.controls['cardHolder'].value as string,
          };
        }
      } else {
        this.toastr.error('Dati mancanti o incorretti.');
      }
    }, 2000);
  }
  adeguateCardNumberValue(event: any) {
    let value: string = this.paymentForm.controls['cardNumber'].value || '';
    if (
      event &&
      event.data != ' ' &&
      event.data != null &&
      !(this.paymentForm.controls['cardNumber'].value as string).endsWith(' ')
    ) {
      if (value.length == 4) {
        this.paymentForm.controls['cardNumber'].setValue(
          this.paymentForm.controls['cardNumber'].value + ' '
        );
      } else if (value.length == 9) {
        this.paymentForm.controls['cardNumber'].setValue(
          this.paymentForm.controls['cardNumber'].value + ' '
        );
      } else if (value.length == 14) {
        this.paymentForm.controls['cardNumber'].setValue(
          this.paymentForm.controls['cardNumber'].value + ' '
        );
      }
    }
    this.paymentForm.controls['cardNumber'].updateValueAndValidity();
  }

  limitExpirationMonth() {
    if (
      this.paymentForm.controls['expirationMonth'].value &&
      String(this.paymentForm.controls['expirationMonth'].value).length > 2
    ) {
      this.paymentForm.controls['expirationMonth'].setValue(
        String(this.paymentForm.controls['expirationMonth'].value).substring(0, 2)
      );
    }
    this.paymentForm.controls['expirationMonth'].updateValueAndValidity();
  }
  limitExpirationYear() {
    if (
      this.paymentForm.controls['expirationYear'].value &&
      String(this.paymentForm.controls['expirationYear'].value).length > 2
    ) {
      this.paymentForm.controls['expirationYear'].setValue(
        String(this.paymentForm.controls['expirationYear'].value).substring(0, 2)
      );
    }
    this.paymentForm.controls['expirationYear'].updateValueAndValidity();
  }
}
