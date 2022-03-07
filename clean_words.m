file = fopen('word.txt');
imported = textscan(file, '%s', 'Delimiter','\n');
fclose(file);

lengths = cell2mat(cellfun(@size, imported{1}, 'UniformOutput', false));

cleaned = imported{1}(lengths(:, 2) <= 16);

writecell(cleaned, 'words.txt');