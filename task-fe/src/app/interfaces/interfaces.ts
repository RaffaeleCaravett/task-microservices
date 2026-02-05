import { ProjectState, sizes, TaskState } from '../enums/enums';

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
export interface login {
  email: string;
  password: string;
}
export interface CompanySignup {
  ragioneSociale: string;
  partitaIva: string;
  formaGiuridica: number;
  paeseDiRegistrazione: number;
  citta: number;
  cap: number;
  regione: number;
  via: string;
  settore: number;
  nomeAzienda: string;
  dimensioniAzienda: number;
  paeseDiRegistrazioneSede: number;
  cittaSede: string;
  capSede: string;
  regioneSede: string;
  viaSede: string;
  pianoId: number;
  subscriptionDays: number;
  metodoPagamentoDTO: metodoPagamento;
  email: string;
  password: string;
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
  immagine: immagine[];
  projects: project[];
  users: User[];
}
export interface immagine {
  name: string;
  image: string;
  uploadedAt: string;
  isCurrent: boolean;
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
  immagine: immagine[];
  color?: string;
}
export interface project {
  id: number;
  createdAt: string;
  state: ProjectState;
  user: User[];
  manager: User;
  task: task[];
  name: string;
  description: string;
}
export interface task {
  id: number;
  creator: User;
  title: string;
  developers: User[];
  state: TaskState;
  createdAt: string;
  description: string;
}
export interface metodoPagamento {
  cardNumber: string;
  month: number;
  year: number;
  secretCode: number;
  owner: string;
}
export interface Page<T> {
  content: T[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: Pageable;
  size: number;
  sort: Sort;
  totalElements: number;
  totalPages: number;
}

export interface Pageable {
  offset: number;
  pageNumber: number;
  pageSize: number;
  paged: boolean;
  unpaged: boolean;
  sort: Sort;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}
export interface accessCode {
  id: number;
  code: string;
  creationTime: string;
  isUsed: boolean;
}

export interface HomeCars {
  id: number;
  title: string;
  description: string;
  icon: string;
}
