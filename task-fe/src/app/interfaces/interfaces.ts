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
  nome: string;
}
export interface nazione {
  id: number;
  nome: string;
}
export interface regione {
  id: number;
  nome: string;
}
export interface citta {
  id: number;
  nome: string;
}
export interface cap {
  id: number;
  cap: string;
}
export interface formaGiuridica {
  id: number;
  nome: string;
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
