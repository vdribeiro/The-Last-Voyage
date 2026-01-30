const CopyWebpackPlugin = require("copy-webpack-plugin");
const path = require("path");

config.resolve.fallback = {
    ...config.resolve.fallback,
    "os": false,
    "fs": false,
    "path": false,
    "crypto": false,
};

config.experiments = {
    ...config.experiments,
    topLevelAwait: true,
    outputModule: true
};

config.devServer.headers = {
    ...config.devServer.headers,
    "Cross-Origin-Embedder-Policy": "require-corp",
    "Cross-Origin-Opener-Policy": "same-origin",
};

config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            { from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.wasm"), to: "." },
            { from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.js"), to: "." },
            { from: path.resolve(__dirname, "../../node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js"), to: "." }
        ]
    })
);