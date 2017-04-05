const jsonToTypescript = require('json-schema-typescript');
var rimraf = require('rimraf');

console.log(__dirname);

var outputPath = `${__dirname}/../src/app/domain`;

rimraf.sync(outputPath, {}, function () { console.log('done'); });

jsonToTypescript(`${__dirname}/schemas/**/*.json`, outputPath)
    .then(files => console.log(files.length, 'TypeScript interfaces generated'))
    .catch(ex => console.error(ex));