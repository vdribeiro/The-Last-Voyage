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
    loader: "string-replace-loader",
    options: {
        search: 'self.locateFile=function\\(e,t\\)\\{return t\\+e\\}',
        replace: 'self.locateFile=function(e,t){return "./" + e}',
        flags: 'g'
    }
});