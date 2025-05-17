export interface MlkitDocScannerPlugin {
  /**
   * Starts the document scanning process.
   * @param options Configuration options for the scanner.
   * @returns A promise that resolves with the scan result.
   */
  scanDocument(options?: ScanOptions): Promise<ScanResult>;
}

/**
 * Options for the document scanner.
 */
export interface ScanOptions {
  /**
   * Whether to allow importing from the photo gallery.
   * @default false
   */
  galleryImportAllowed?: boolean;
  /**
   * The maximum number of pages that can be scanned.
   * @default 10
   */
  pageLimit?: number;
  /**
   * The desired result formats.
   * Can be 'JPEG', 'PDF', or 'JPEG_PDF'.
   * @default 'JPEG_PDF'
   */
  resultFormats?: 'JPEG' | 'PDF' | 'JPEG_PDF';
  /**
   * The scanner mode.
   * Can be 'FULL', 'BASE', or 'BASE_WITH_FILTER'.
   * 'FULL': Enables auto-capture and file format selection (if multiple formats are specified).
   * 'BASE': Disables auto-capture and file format selection. User always needs to tap capture button.
   * 'BASE_WITH_FILTER': Enables import from gallery, but otherwise same as 'BASE'.
   * @default 'FULL'
   */
  scannerMode?: 'FULL' | 'BASE' | 'BASE_WITH_FILTER';
}

/**
 * Information about a generated PDF document.
 */
export interface PdfInfo {
  /**
   * The URI of the generated PDF file.
   */
  uri: string;
  /**
   * The number of pages in the PDF.
   */
  pageCount: number;
}

/**
 * Result of a document scan operation.
 */
export interface ScanResult {
  /**
   * An array of URIs for the scanned image pages (JPEG).
   * Present if 'JPEG' or 'JPEG_PDF' was requested in resultFormats.
   */
  scannedImages?: string[];
  /**
   * Information about the generated PDF.
   * Present if 'PDF' or 'JPEG_PDF' was requested in resultFormats.
   */
  pdf?: PdfInfo;
}
