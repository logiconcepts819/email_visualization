scores = Hash.new(0)

File.open("SentiWordNet_3.0.0.txt") do |file|
  file.each_line do |line|
    next if line[0] == "#"
    line_parts = line.split(/\t/)
    pos_score = line_parts[2].to_f
    neg_score = line_parts[3].to_f
    words = line_parts[4].split(' ').map {|w| w.split('#').first}

    words.each do |word|
      if scores[word].nil?
        scores[word] = (pos_score - neg_score)
      else
        scores[word] = scores[word] + (pos_score - neg_score)
      end
    end
  end
end

File.open("clean_senti_words.txt", 'w') do |file|
  scores.each {|k,v| file.write("#{k} #{v}\n")}
end
