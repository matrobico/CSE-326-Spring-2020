class User < ApplicationRecord
    has_and_belongs_to_many :groups
    has_secure_password
end