export interface SumUpPlugin {
  login(options: LoginOptions): Promise<SumUpResponse>;
  checkout(options: CheckoutOptions): Promise<SumUpResponse>;
}

export interface LoginOptions {
  affiliateKey: string,
  accessToken?: string,
}

export interface CheckoutOptions {
  total: number,
  currency: string,
  title?: string,
  receiptEmail?: string,
  receiptSMS?: string,
  additionalInfo?: { [key: string]: string },
  foreignTransactionId?: string,
  skipSuccessScreen?: boolean
}

export interface SumUpResponse {
  code: number,
  message: string
}
