const CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require('path');

config.resolve.fallback = {
    ...config.resolve.fallback,
    fs: false,
    path: false,
    crypto: false,
};

config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            {
                from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.wasm"),
                to: "."
            }
        ]
    })
);

config.module.rules.push({
    test: /sqljs\.worker\.js$/,
    include: [
        path.resolve(__dirname, "../../node_modules/@cashapp/sqldelight-sqljs-worker")
    ],
    use: [
        {
            loader: 'string-replace-loader',
            options: {
                search: /self\.locateFile\s*=\s*(?:function\s*\(e,t\)\s*\{\s*return\s*t\s*\+\s*e\s*\}|\(path,\s*prefix\)\s*=>\s*prefix\s*\+\s*path)/g,
                replace: 'self.locateFile = (path, prefix) => "./" + path',
            }
        }
    ]
});