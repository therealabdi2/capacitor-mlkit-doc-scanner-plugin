package com.capacitor.mlkit.doc.scanner;

import android.app.Activity;
import android.content.IntentSender;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner;
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

@CapacitorPlugin(name = "MlkitDocScanner")
public class MlkitDocScannerPlugin extends Plugin {

    private ActivityResultLauncher<IntentSenderRequest> scannerLauncher;
    private PluginCall savedCall;

    @Override
    public void load() {
        super.load();
        scannerLauncher =
            getActivity()
                .registerForActivityResult(
                    new ActivityResultContracts.StartIntentSenderForResult(),
                    activityResult -> {
                        if (savedCall == null) {
                            return;
                        }
                        if (activityResult.getResultCode() == Activity.RESULT_OK) {
                            if (activityResult.getData() == null) {
                                savedCall.reject("Scan completed but no data was returned.");
                                savedCall = null;
                                return;
                            }
                            GmsDocumentScanningResult gmsResult = GmsDocumentScanningResult.fromActivityResultIntent(activityResult.getData());
                            if (gmsResult != null) {
                                JSObject ret = new JSObject();
                                List<String> imageUris = new ArrayList<>();
                                if (gmsResult.getPages() != null) {
                                    for (GmsDocumentScanningResult.Page page : gmsResult.getPages()) {
                                        imageUris.add(page.getImageUri().toString());
                                    }
                                }
                                // only add 'scannedImages' if it's not empty, or if JPEG was requested.
                                ret.put("scannedImages", new JSONArray(imageUris));

                                if (gmsResult.getPdf() != null) {
                                    JSObject pdfInfo = new JSObject();
                                    pdfInfo.put("uri", gmsResult.getPdf().getUri().toString());
                                    pdfInfo.put("pageCount", gmsResult.getPdf().getPageCount());
                                    ret.put("pdf", pdfInfo);
                                }
                                savedCall.resolve(ret);
                            } else {
                                savedCall.reject("Failed to retrieve scanning result.");
                            }
                        } else {
                            savedCall.reject("Scan cancelled or failed. Result code: " + activityResult.getResultCode());
                        }
                        savedCall = null;
                    }
                );
    }

    @PluginMethod
    public void scanDocument(PluginCall call) {
        this.savedCall = call;

        boolean galleryImportAllowed = call.getBoolean("galleryImportAllowed", false);
        int pageLimit = call.getInt("pageLimit", 10);
        String resultFormatsOption = call.getString("resultFormats", "JPEG_PDF");
        String scannerModeOption = call.getString("scannerMode", "FULL");

        int GmsScannerMode;
        switch (scannerModeOption.toUpperCase()) {
            case "BASE":
                GmsScannerMode = GmsDocumentScannerOptions.SCANNER_MODE_BASE;
                break;
            case "BASE_WITH_FILTER":
                GmsScannerMode = GmsDocumentScannerOptions.SCANNER_MODE_BASE_WITH_FILTER;
                break;
            case "FULL":
            default:
                GmsScannerMode = GmsDocumentScannerOptions.SCANNER_MODE_FULL;
                break;
        }

        GmsDocumentScannerOptions.Builder optionsBuilder = new GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(galleryImportAllowed)
            .setPageLimit(pageLimit)
            .setScannerMode(GmsScannerMode);

        // set result formats based on the option, calling the varargs method directly
        switch (resultFormatsOption.toUpperCase()) {
            case "JPEG":
                optionsBuilder.setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG);
                break;
            case "PDF":
                optionsBuilder.setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF);
                break;
            case "JPEG_PDF":
            default:
                optionsBuilder.setResultFormats(
                    GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                    GmsDocumentScannerOptions.RESULT_FORMAT_PDF
                );
                break;
        }

        GmsDocumentScanner scanner = GmsDocumentScanning.getClient(optionsBuilder.build());

        scanner
            .getStartScanIntent(getActivity())
            .addOnSuccessListener(
                intentSender -> {
                    scannerLauncher.launch(new IntentSenderRequest.Builder(intentSender).build());
                }
            )
            .addOnFailureListener(
                e -> {
                    savedCall.reject("Failed to start scan intent: " + e.getMessage());
                    savedCall = null;
                }
            );
    }
}
