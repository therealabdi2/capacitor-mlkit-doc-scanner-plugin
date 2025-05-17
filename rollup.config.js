import commonjs from '@rollup/plugin-commonjs';
import { nodeResolve } from '@rollup/plugin-node-resolve';
import typescript from 'rollup-plugin-typescript2';

export default {
  input: 'src/index.ts', // Or 'src/web.ts' if that's your main web entry
  output: [
    {
      file: 'dist/plugin.js',
      format: 'iife',
      name: 'capacitorMlkitDocScanner', // Global variable for IIFE bundle
      globals: {
        '@capacitor/core': 'capacitorExports',
      },
      sourcemap: true,
      inlineDynamicImports: true,
    },
    {
      file: 'dist/plugin.cjs.js',
      format: 'cjs',
      sourcemap: true,
      inlineDynamicImports: true,
    },
    {
      file: 'dist/plugin.esm.js',
      format: 'es',
      sourcemap: true,
      inlineDynamicImports: true,
    },
  ],
  external: ['@capacitor/core'],
  plugins: [
    nodeResolve(), // Helps Rollup find external modules
    commonjs(), // Converts CommonJS modules to ES6
    typescript({
      // Compiles TypeScript files
      tsconfigOverride: {
        compilerOptions: {
          outDir: 'dist',
          declaration: true,
          declarationDir: 'dist/definitions',
        },
      },
    }),
  ],
};
