import { registerPlugin } from "@capacitor/core";

import type { MlkitDocScannerPlugin } from "./definitions";

const MlkitDocScanner = registerPlugin<MlkitDocScannerPlugin>(
  "MlkitDocScanner",
  {
    web: () => import("./web").then((m) => new m.MlkitDocScannerWeb()),
  },
);

export * from "./definitions";
export { MlkitDocScanner };
