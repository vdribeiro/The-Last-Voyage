const CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require('path');

config.resolve.fallback = {
    ...config.resolve.fallback,
    fs: false,
    path: false,
    crypto: false,
};

config.resolve.alias = {
    ...config.resolve.alias,
    'sqljs.worker.js': path.resolve(__dirname, "../../node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js")
};

config.module.rules.push({
    test: /sqljs\.worker\.js$/,
    type: 'asset/resource',
    generator: {
        filename: 'sqljs.worker.js'
    }
});

config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            {
                from: path.resolve(__dirname, "../../node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js"),
                to: "sqljs.worker.js"
            },
            {
                from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.wasm"),
                to: "sql-wasm.wasm"
            }
        ]
    })
);