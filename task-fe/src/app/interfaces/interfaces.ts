export interface UserLogin {
  email: string;
  password: string;
}
export interface Token {
  email: string;
  password: string;
}

export interface CompanySignup {
  ragioneSociale: string;
  partitaIva: string;
  formaGiuridica: number;
  paeseDiRegistrazione: number;
  indirizzo: {
    citta: number;
    cap: number;
    regione: number;
    via: string;
  };
  settore: number;
  dimensioniAzienda: number;
  sedeOperativa?: {
    citta: string;
    cap: string;
    regione: string;
    via: string;
  };
}
export interface settore {
  id: number;
  name: string;
}
export interface nazione {
  id: number;
  name: string;
}
export interface regione {
  id: number;
  name: string;
}
export interface citta {
  id: number;
  name: string;
}
export interface cap {
  id: number;
  name: string;
}
export interface formaGiuridica {
  id: number;
  name: string;
}
export interface indirizzo {
  id: number;
  citta: citta;
  cap: cap;
  regione: regione;
  via: string;
}
export interface Company {
  id: number;
  createdAt: string;
  isActive: boolean;
  isConfirmed: boolean;
  deletedAt?: string;
  role: string;
  ragioneSociale: string;
  partitaIva: string;
  formaGiuridica: formaGiuridica;
  paeseDiRegistrazione: nazione;
  indirizzo: indirizzo;
  settore: settore;
  dimensioniAzienda: number;
  sedeOperativa?: indirizzo;
}
