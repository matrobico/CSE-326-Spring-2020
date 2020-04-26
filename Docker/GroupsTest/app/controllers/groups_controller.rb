class GroupsController < ApplicationController
    before_action :authenticate_request
    before_action :set_group, except: [:create]
    before_action :authenticate_user, except: [:create, :adduser]

    def index
        @groups = Group.all

        render json: @groups

      end
    
    def show
      @group = Group.find(params[:id])
    end

    def new
        @group = Group.new
    end

    def create
        @group = Group.new(group_params)
     
        if @group.save
          render json: {message: 'Worked!', group: @group}
          #redirect_to @group
        else
          render 'new'
        end
    end

    def users
      render json: @group.users.select(:name)
    end  
    
    def adduser
      @param = params[:password]
      if @param == @group.password
        if @group.users.find_by(id: @current_user.id) == nil
          @group.users << @current_user
          render json: { error: 'Worked!'}
        else
          render json: { error: 'User already added'}
        end
      else
        render json: { error: 'Failed!'}
      end

    end  

    private
    def authenticate_user
      @current_user = AuthorizeApiRequest.call(request.headers).result
      render json: { error: 'Not Authorized (in group)' }, status: 401 unless @group.users.find_by(id: @current_user.id)
    end  

    def set_group
      @group = Group.find(params[:group_id])
    end

    def group_params
      params.permit(:title, :password)
    end
end
