import { WebPlugin } from '@capacitor/core';

import type { MlkitDocScannerPlugin, ScanResult } from './definitions';

export class MlkitDocScannerWeb extends WebPlugin implements MlkitDocScannerPlugin {
  async scanDocument(): Promise<ScanResult> {
    console.warn('Document scanning is not available on the web.');
    throw this.unavailable('Document scanning is not available on the web.');
  }
}
