import { sizes } from '../enums/enums';

export interface UserLogin {
  email: string;
  password: string;
}
export interface Token {
  email: string;
  password: string;
}
export interface token {
  accessToken: string;
  refreshToken: string;
}
export interface loginSuccess {
  token: token;
  company: Company | null;
  user: User | null;
}
export interface CompanySignup {
  ragioneSociale: string;
  partitaIva: string;
  formaGiuridica: number;
  nazione: number;
  citta: number;
  cap: number;
  regione: number;
  via: string;
  settore: number;
  dimensioniAzienda: number;
  nazioneSede: number;
  cittaSede: string;
  capSede: string;
  regioneSede: string;
  viaSede: string;
  pianoId: number;
  subscriptionDays: number;
  metodoPagamentoDTO: metodoPagamento;
}
export interface CompanyDTOFromSignup {
  id: number;
  email: string;
  nomeAzienda: string;
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
export interface dimensioni {
  id: number;
  label: string;
  dimensione: sizes;
}
export interface piano {
  id: number;
  titolo: string;
  prezzo: number;
  description: string;
}
export interface indirizzo {
  id: number;
  nazione: nazione;
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
  deleteddAt?: string;
  role: string;
  ragioneSociale: string;
  partitaIva: string;
  formaGiuridica: formaGiuridica;
  indirizzo: indirizzo;
  settore: settore;
  dimensioniAzienda: dimensioni;
  sedeOperativa?: indirizzo;
  email: string;
}
export interface User {
  id: number;
  createdAt: string;
  isActive: boolean;
  isConfirmed: boolean;
  deleteddAt?: string;
  role: string;
  nome: string;
  cognome: string;
  email: string;
  companies: Company[];
}
export interface metodoPagamento {
  cardNumber: string;
  month: number;
  year: number;
  secretCode: number;
  owner: string;
}
