const jsonToTypescript = require('json-schema-typescript');

console.log(__dirname);

jsonToTypescript(`${__dirname}/schemas/**/*.json`, `${__dirname}/../src/app/domain`)
    .then(files => console.log(files.length, 'TypeScript interfaces generated'))
    .catch(ex => console.error(ex));