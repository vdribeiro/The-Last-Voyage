const CopyWebpackPlugin = require("copy-webpack-plugin");
const path = require("path");
const os = require("os");
const dist = path.resolve("../../node_modules/sql.js/dist/");
const wasm = path.join(dist, "sql-wasm.wasm");

if (config.files) {
    config.files.push({
        pattern: wasm,
        served: true,
        watched: false,
        included: false,
        nocache: false,
    });

    if (config.proxies) {
        config.proxies["/sql-wasm.wasm"] = path.join("/absolute/", wasm);
    }

    const output = {
        path: path.join(os.tmpdir(), '_karma_webpack_') + Math.floor(Math.random() * 1000000),
    };

    if (typeof config.set === 'function') {
        config.set({
            webpack: { ...config.webpack, output }
        });
    }

    config.files.push({
        pattern: `${output.path}/**/*`,
        watched: false,
        included: false,
    });
}

if (config.resolve) {
    config.resolve.fallback = {
        ...config.resolve.fallback,
        fs: false,
        path: false,
        crypto: false,
    };
}

if (config.devServer) {
    config.devServer = {
        ...config.devServer,
        headers: {
            "Cross-Origin-Embedder-Policy": "require-corp",
            "Cross-Origin-Opener-Policy": "same-origin",
        }
    };
}

if (!config.plugins) {
    config.plugins = [];
}

config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            {
                from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.wasm"),
                to: "."
            },
            {
                from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.js"),
                to: "."
            },
            {
                from: path.resolve(__dirname, "../../node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js"),
                to: "."
            }
        ]
    })
);
