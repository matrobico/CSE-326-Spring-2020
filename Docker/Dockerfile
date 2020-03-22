FROM ruby:2.5.7
RUN apt -y update
RUN apt -y upgrade
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
RUN echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list
RUN apt-get -y update  
RUN apt-get -y install yarn
RUN yarn install
RUN gem install rails 
RUN apt -y install sqlite3
RUN mkdir Ephemeral