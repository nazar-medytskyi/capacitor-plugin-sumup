import { WebPlugin } from '@capacitor/core';

import type { 
  SumUpPlugin,
  LoginOptions,
  CheckoutOptions,
  SumUpResponse
} from './definitions';

export class SumUpWeb extends WebPlugin implements SumUpPlugin {
  async login(options: LoginOptions): Promise<SumUpResponse> {
    console.debug('login options: ', options);
    return Promise.resolve({
      code: 0,
      message: 'Web version of SumUp not available.'
    })
  }

  async checkout(options: CheckoutOptions): Promise<SumUpResponse> {
    console.debug('checkout options: ', options);
    return Promise.resolve({
      code: 0,
      message: 'Web version of SumUp not available.'
    })
  }
}
