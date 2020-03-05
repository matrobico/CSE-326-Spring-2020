class MessagesController < ApplicationController
skip_before_action :verify_authenticity_token

  def index
    @message = Message.all
  end

  def show
    @message = Message.find(params[:id])
  end

def new
end

def create
  @message = Message.new(message_params)
 
  @message.save
  redirect_to @message
end

private
  def message_params
    params.require(:message).permit(:title, :text)
  end

end
