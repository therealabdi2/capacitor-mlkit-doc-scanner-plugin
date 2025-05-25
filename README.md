# Capacitor ML Kit Document Scanner Plugin

This plugin allows you to use Google's ML Kit Document Scanner in your Capacitor applications. It provides an easy way to add a document scanning feature, allowing users to scan documents and receive them as JPEG images and/or PDF files.

## Demo

A demo application showcasing the capabilities of this plugin can be found here: [https://github.com/therealabdi2/demo-mlkit-doc-scanner](https://github.com/therealabdi2/demo-mlkit-doc-scanner)

## Installation

```bash
npm install capacitor-mlkit-doc-scanner
npx cap sync
```

### Android

1.  Add the ML Kit Document Scanner dependency to your app's `build.gradle` file (`android/app/build.gradle`):

    ```gradle
    dependencies {
        // ...other dependencies
        implementation 'com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1'
    }
    ```

2.  Ensure your project's `minSdkVersion` is 21 or higher in `android/app/build.gradle`.
    The ML Kit Document Scanner also requires a minimal device total RAM of 1.7GB.

## Supported Platforms

- [x] Android
- [ ] iOS (Not available)
- [ ] Web (Not available - native feature)

## API

<docgen-index>

- [`scanDocument(...)`](#scandocument)

</docgen-index>

<docgen-api>
<!--Update the API documentation below after generating it with `npm run docgen`-->

### scanDocument(...)

```typescript
scanDocument(options?: ScanOptions) => Promise<ScanResult>
```

Starts the document scanning process.

**Parameters:**

| Name      | Type          | Description                            | Optional |
| :-------- | :------------ | :------------------------------------- | :------- |
| `options` | `ScanOptions` | Configuration options for the scanner. | Yes      |

**Returns:** `Promise<ScanResult>`

---

**ScanOptions Interface:**

| Name                   | Type                                   | Description                                                                                                                                                                                                                                                                                                                                                    | Default    |
| :--------------------- | :------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :--------- |
| `galleryImportAllowed` | `boolean`                              | Whether to allow importing from the photo gallery.                                                                                                                                                                                                                                                                                                             | `false`    |
| `pageLimit`            | `number`                               | The maximum number of pages that can be scanned.                                                                                                                                                                                                                                                                                                               | `10`       |
| `resultFormats`        | `'JPEG' | 'PDF' | 'JPEG_PDF'`          | The desired result formats.                                                                                                                                                                                                                                                                                                                                    | `'JPEG_PDF'` |
| `scannerMode`          | `'FULL' | 'BASE' | 'BASE_WITH_FILTER'` | The scanner mode. <br/>`SCANNER_MODE_BASE`: Basic editing capabilities (crop, rotate, reorder pages, etc.). <br/>`SCANNER_MODE_BASE_WITH_FILTER`: Adds image filters (grayscale, auto image enhancement, etc.) to the `SCANNER_MODE_BASE` mode. <br/>`SCANNER_MODE_FULL` (default): Adds ML-enabled image cleaning capabilities (erase stains, fingers, etc.) to the `SCANNER_MODE_BASE_WITH_FILTER` mode. This mode will also allow future major features to be automatically added along with Google Play services updates, while the other two modes will maintain their current feature sets and only receive minor refinements. | `'FULL'`     |

**ScanResult Interface:**

| Name            | Type       | Description                                                                                         |
| :-------------- | :--------- | :-------------------------------------------------------------------------------------------------- |
| `scannedImages` | `string[]` | An array of URIs for the scanned image pages (JPEG). Present if 'JPEG' or 'JPEG_PDF' was requested. |
| `pdf`           | `object`   | Information about the generated PDF. Present if 'PDF' or 'JPEG_PDF' was requested.                  |
| `pdf.uri`       | `string`   | The URI of the generated PDF file.                                                                  |
| `pdf.pageCount` | `number`   | The number of pages in the PDF.                                                                     |

</docgen-api>

## Usage Example

```typescript
import { MlkitDocScanner } from 'capacitor-mlkit-doc-scanner';

async function startScan() {
  try {
    const result = await MlkitDocScanner.scanDocument({
      galleryImportAllowed: true,
      pageLimit: 5,
      resultFormats: 'JPEG_PDF',
      scannerMode: 'FULL',
    });

    console.log('Scan successful:', result);

    if (result.scannedImages && result.scannedImages.length > 0) {
      console.log('First image URI:', result.scannedImages[0]);
      // Handle image URIs (e.g., display them)
    }

    if (result.pdf) {
      console.log('PDF URI:', result.pdf.uri);
      console.log('PDF Page Count:', result.pdf.pageCount);
      // Handle PDF URI (e.g., open or upload it)
    }
  } catch (error) {
    console.error('Scan failed:', error);
  }
}
```

## Important Notes

- The ML Kit Document Scanner models, scanning logic, and UI flow are dynamically downloaded by Google Play services. Users might have to wait for these to download before the first use.
- This API requires Android API level 21 or above.
- It also requires a minimal device total RAM of 1.7GB. If lower, it returns an `MlKitException` with error code `UNSUPPORTED` when calling the API (this plugin will reject the promise).
- Consider that generating document files takes time and requires processing power, so only request the output formats (JPEG, or PDF, or both) you actually need via the `resultFormats` option.

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests to [https://github.com/therealabdi2/capacitor-mlkit-doc-scanner-plugin](https://github.com/therealabdi2/capacitor-mlkit-doc-scanner-plugin).

## License

This plugin is licensed under the MIT License.
