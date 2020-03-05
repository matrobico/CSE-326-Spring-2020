class ApplicationController < ActionController::Base
  def default_url_options
    if Rails.env.development?
      {:host => "eph.nopesled.com"}
    else  
      {}
    end
  end

  def show
    @message = Message.find(params[:id])
  end

  def new
  end

end
