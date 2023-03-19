import pygame.image

from const import SPEED_INCREASE_RATE, JUMP_POWER, SPEED_DECREASE_RATE, MAX_FASTMOVE_SPEED, MAX_MOVE_SPEED, GRAVITY, \
    LOW_JUMP_MULTIPLIER, FALL_MULTIPLIER, MAX_FALL_SPEED


class Player:

    def __init__(self, x, y):
        self.num_of_lives = 3
        self.score = 0
        self.coins = 0

        self.visible = True
        self.sprite_tick = 0
        self.power_level = 0

        self.unkillable = False
        self.unkillable_time = 0

        self.in_level_up_animation = False
        self.in_level_up_animation_time = 0
        self.in_level_down_animation = False
        self.in_level_down_animation_time = 0

        self.already_jumped = False
        self.next_jump_time = 0
        self.next_fireball_time = 0
        self.x_vel = 0
        self.y_vel = 0
        self.direction = True
        self.on_ground = False
        self.fast_moving = False

        self.pos_x = x
        self.image = pygame.image.load("images/mario/mario.png").convert_alpha()
        self.sprites = []
        self.load_sprites()

        self.rect = pygame.Rect(x, y, 32, 32)

    def load_sprites(self):
        self.sprites = [
            # 0 Small, stay
            pygame.image.load('images/mario/mario.png'),

            # 1 Small, move 0
            pygame.image.load('images/mario/mario_move0.png'),

            # 2 Small, move 1
            pygame.image.load('images/mario/mario_move1.png'),

            # 3 Small, move 2
            pygame.image.load('images/mario/mario_move2.png'),

            # 4 Small, jump
            pygame.image.load('images/mario/mario_jump.png'),

            # 5 Small, end 0
            pygame.image.load('images/mario/mario_end.png'),

            # 6 Small, end 1
            pygame.image.load('images/mario/mario_end1.png'),

            # 7 Small, stop
            pygame.image.load('images/mario/mario_st.png'),

            # =============================================

            # 8 Big, stay
            pygame.image.load('images/mario/mario1.png'),

            # 9 Big, move 0
            pygame.image.load('images/mario/mario1_move0.png'),

            # 10 Big, move 1
            pygame.image.load('images/mario/mario1_move1.png'),

            # 11 Big, move 2
            pygame.image.load('images/mario/mario1_move2.png'),

            # 12 Big, jump
            pygame.image.load('images/mario/mario1_jump.png'),

            # 13 Big, end 0
            pygame.image.load('images/mario/mario1_end.png'),

            # 14 Big, end 1
            pygame.image.load('images/mario/mario1_end1.png'),

            # 15 Big, stop
            pygame.image.load('images/mario/mario1_st.png'),

            # =============================================

            # 16 Big_fireball, stay
            pygame.image.load('images/mario/mario2.png'),

            # 17 Big_fireball, move 0
            pygame.image.load('images/mario/mario2_move0.png'),

            # 18 Big_fireball, move 1
            pygame.image.load('images/mario/mario2_move1.png'),

            # 19 Big_fireball, move 2
            pygame.image.load('images/mario/mario2_move2.png'),

            # 20 Big_fireball, jump
            pygame.image.load('images/mario/mario2_jump.png'),

            # 21 Big_fireball, end 0
            pygame.image.load('images/mario/mario2_end.png'),

            # 22 Big_fireball, end 1
            pygame.image.load('images/mario/mario2_end1.png'),

            # 23 Big_fireball, stop
            pygame.image.load('images/mario/mario2_st.png'),
        ]

        # Left side
        for i in range(len(self.sprites)):
            self.sprites.append(pygame.transform.flip(self.sprites[i], 180, 0))

        # Power level changing, right
        self.sprites.append(pygame.image.load("images/mario/mario_lvlup.png").convert_alpha())

        # Power level changing, left
        self.sprites.append(pygame.transform.flip(self.sprites[-1], 180, 0))

        # Death
        self.sprites.append(pygame.image.load('images/mario/mario_death.png').convert_alpha())

    def update(self, game):
        self.player_physics(game)
        self.update_image(game)
        self.update_unkillable_time()

    def player_physics(self, game):
        if game.keyR:
            self.x_vel += SPEED_INCREASE_RATE
            self.direction = True
        if game.keyL:
            self.x_vel -= SPEED_INCREASE_RATE
            self.direction = False
        if not game.keyU:
            self.already_jumped = False
        elif game.keyU:
            if self.on_ground and not self.already_jumped:
                self.y_vel = -JUMP_POWER
                self.already_jumped = True
                self.next_jump_time = pygame.time.get_ticks() + 750
                if self.power_level >= 1:
                    game.sound_manager.play('big_mario_jump', 0, 0.5)
                else:
                    game.sound_manager.play('small_mario_jump', 0, 0.5)

        # Fireball shoot and fast moving
        self.fast_moving = False
        if game.keyShift:
            self.fast_moving = True
            if self.power_level == 2:
                if pygame.time.get_ticks() > self.next_fireball_time:
                    if not (self.in_level_up_animation or self.in_level_down_animation):
                        if len(game.world.projectiles) < 2:
                            self.shoot_fireball(game, self.rect.x, self.rect.y, self.direction)

        if not (game.keyR or game.keyL):
            if self.x_vel > 0:
                self.x_vel -= SPEED_DECREASE_RATE
            elif self.x_vel < 0:
                self.x_vel += SPEED_DECREASE_RATE
        else:
            if self.x_vel > 0:
                if self.fast_moving:
                    if self.x_vel > MAX_FASTMOVE_SPEED:
                        self.x_vel = MAX_FASTMOVE_SPEED
                else:
                    if self.x_vel > MAX_MOVE_SPEED:
                        self.x_vel = MAX_MOVE_SPEED
            if self.x_vel < 0:
                if self.fast_moving:
                    if (-self.x_vel) > MAX_FASTMOVE_SPEED: self.x_vel = -MAX_FASTMOVE_SPEED
                else:
                    if (-self.x_vel) > MAX_MOVE_SPEED:
                        self.x_vel = -MAX_MOVE_SPEED

        # removing the computational error
        if 0 < self.x_vel < SPEED_DECREASE_RATE:
            self.x_vel = 0
        if 0 > self.x_vel > -SPEED_DECREASE_RATE:
            self.x_vel = 0

        if not self.on_ground:
            # Moving up, button is pressed
            if self.y_vel < 0 and game.keyU:
                self.y_vel += GRAVITY

            # Moving up, button is not pressed - low jump
            elif self.y_vel < 0 and not game.keyU:
                self.y_vel += GRAVITY * LOW_JUMP_MULTIPLIER

            # Moving down
            else:
                self.y_vel += GRAVITY * FALL_MULTIPLIER

            if self.y_vel > MAX_FALL_SPEED:
                self.y_vel = MAX_FALL_SPEED

        blocks = game.world.get_blocks_for_collision(self.rect.x // 32, self.rect.y // 32)

        self.pos_x += self.x_vel
        self.rect.x = self.pos_x

        self.update_x_pos(blocks)

        self.rect.y += self.y_vel
        self.update_y_pos(blocks, game)

        # on_ground parameter won't be stable without this piece of code
        coord_y = self.rect.y // 32
        if self.power_level > 0:
            coord_y += 1
        for block in game.world.get_blocks_below(self.rect.x // 32, coord_y):
            if block != 0 and block.type != 'BGObject':
                if pygame.Rect(self.rect.x, self.rect.y + 1, self.rect.w, self.rect.h).colliderect(block.rect):
                    self.on_ground = True

        # Map border check
        if self.rect.y > 448:
            game.world.player_death(game)

        # End Flag collision check
        if self.rect.colliderect(game.world.flag.pillar_rect):
            game.world.player_win(game)
            
    def set_image(self, image_id):
        # "Dead" sprite
        if image_id == len(self.sprites):
            self.image = self.sprites[-1]

        elif self.direction:
            self.image = self.sprites[image_id + self.power_level * 8]
        else:
            self.image = self.sprites[image_id + self.power_level * 8 + 24]

    def update_image(self, game):

        self.sprite_tick += 1
        if game.keyShift:
            self.sprite_tick += 1

        if self.power_level in (0, 1, 2):

            if self.x_vel == 0:
                self.set_image(0)
                self.sprite_tick = 0

            # Player is running
            elif (
                    ((self.x_vel > 0 and game.keyR and not game.keyL) or
                     (self.x_vel < 0 and game.keyL and not game.keyR)) or
                    (self.x_vel > 0 and not (game.keyL or game.keyR)) or
                    (self.x_vel < 0 and not (game.keyL or game.keyR))
            ):

                if self.sprite_tick > 30:
                    self.sprite_tick = 0

                if self.sprite_tick <= 10:
                    self.set_image(1)
                elif 11 <= self.sprite_tick <= 20:
                    self.set_image(2)
                elif 21 <= self.sprite_tick <= 30:
                    self.set_image(3)
                elif self.sprite_tick == 31:
                    self.sprite_tick = 0
                    self.set_image(1)

            # Player decided to move in the another direction, but hasn't stopped yet
            elif (self.x_vel > 0 and game.keyL and not game.keyR) or (self.x_vel < 0 and game.keyR and not game.keyL):
                self.set_image(7)
                self.sprite_tick = 0

            if not self.on_ground:
                self.sprite_tick = 0
                self.set_image(4)
    
    def update_unkillable_time(self):
        if self.unkillable:
            self.unkillable_time -= 1
            if self.unkillable_time == 0:
                self.unkillable = False

    def update_x_pos(self, blocks):
        for block in blocks:
            if block != 0 and block.type != 'BGObject':
                block.debugLight = True
                if pygame.Rect.colliderect(self.rect, block.rect):
                    if self.x_vel > 0:
                        self.rect.right = block.rect.left
                        self.pos_x = self.rect.left
                        self.x_vel = 0
                    elif self.x_vel < 0:
                        self.rect.left = block.rect.right
                        self.pos_x = self.rect.left
                        self.x_vel = 0

    def update_y_pos(self, blocks, game):
        self.on_ground = False
        for block in blocks:
            if block != 0 and block.type != 'BGObject':
                if pygame.Rect.colliderect(self.rect, block.rect):

                    if self.y_vel > 0:
                        self.on_ground = True
                        self.rect.bottom = block.rect.top
                        self.y_vel = 0

                    elif self.y_vel < 0:
                        self.rect.top = block.rect.bottom
                        self.y_vel = -self.y_vel / 3
                        self.activate_block_action(game, block)
                        
    def activate_block_action(self, game, block):
        # Question Block
        if block.type_id == 22:
            game.sound_manager.play('block_hit', 0, 0.5)
            if not block.is_activated:
                block.spawn_bonus(game)

        # Brick Platform
        elif block.type_id == 23:
            if self.power_level == 0:
                block.shaking = True
                game.sound_manager.play('block_hit', 0, 0.5)
            else:
                block.destroy(game)
                game.sound_manager.play('brick_break', 0, 0.5)
                self.add_sgame(50)
                
    def reset(self, reset_all):
        self.direction = True
        self.rect.x = 96
        self.pos_x = 96
        self.rect.y = 351
        if self.power_level != 0:
            self.power_level = 0
            self.rect.y += 32
            self.rect.h = 32

        if reset_all:
            self.sgame = 0
            self.coins = 0
            self.num_of_lives = 3

            self.visible = True
            self.sprite_tick = 0
            self.power_level = 0
            self.in_level_up_animation = False
            self.in_level_up_animation_time = 0

            self.unkillable = False
            self.unkillable_time = 0

            self.in_level_down_animation = False
            self.in_level_down_animation_time = 0

            self.already_jumped = False
            self.x_vel = 0
            self.y_vel = 0
            self.on_ground = False
            
    def reset_jump(self):
        self.y_vel = 0
        self.already_jumped = False

    def reset_move(self):
        self.x_vel = 0
        self.y_vel = 0

    def jump_on_mob(self):
        self.already_jumped = True
        self.y_vel = -4
        self.rect.y -= 6
        
    def set_powerlvl(self, power_lvl, game):
        if self.power_level == 0 == power_lvl and not self.unkillable:
            game.world.player_death(game)
            self.in_level_up_animation = False
            self.in_level_down_animation = False

        elif self.power_level == 0 and self.power_level < power_lvl:
            self.power_level = 1
            game.sound_manager.play('mushroom_eat', 0, 0.5)
            game.world.spawn_sgame_text(self.rect.x + 16, self.rect.y, sgame=1000)
            self.add_sgame(1000)
            self.in_level_up_animation = True
            self.in_level_up_animation_time = 61

        elif self.power_level == 1 and self.power_level < power_lvl:
            game.sound_manager.play('mushroom_eat', 0, 0.5)
            game.world.spawn_sgame_text(self.rect.x + 16, self.rect.y, sgame=1000)
            self.add_sgame(1000)
            self.power_level = 2

        elif self.power_level > power_lvl:
            game.sound_manager.play('pipe', 0, 0.5)
            self.in_level_down_animation = True
            self.in_level_down_animation_time = 200
            self.unkillable = True
            self.unkillable_time = 200

        else:
            game.sound_manager.play('mushroom_eat', 0, 0.5)
            game.world.spawn_sgame_text(self.rect.x + 16, self.rect.y, sgame=1000)
            self.add_sgame(1000)
            
    def change_powerlvl_animation(self):

        if self.in_level_down_animation:
            self.in_level_down_animation_time -= 1

            if self.in_level_down_animation_time == 0:
                self.in_level_down_animation = False
                self.visible = True
            elif self.in_level_down_animation_time % 20 == 0:
                if self.visible:
                    self.visible = False
                else:
                    self.visible = True
                if self.in_level_down_animation_time == 100:
                    self.power_level = 0
                    self.rect.y += 32
                    self.rect.h = 32

        elif self.in_level_up_animation:
            self.in_level_up_animation_time -= 1

            if self.in_level_up_animation_time == 0:
                self.in_level_up_animation = False
                self.rect.y -= 32
                self.rect.h = 64

            elif self.in_level_up_animation_time in (60, 30):
                self.image = self.sprites[-3] if self.direction else self.sprites[-2]
                self.rect.y -= 16
                self.rect.h = 48

            elif self.in_level_up_animation_time in (45, 15):
                self.image = self.sprites[0] if self.direction else self.sprites[24]
                self.rect.y += 16
                self.rect.h = 32
                
    def flag_animation_move(self, game, walk_to_castle):
        if walk_to_castle:
            self.direction = True

            if not self.on_ground:
                self.y_vel += GRAVITY if self.y_vel <= MAX_FALL_SPEED else 0

            x = self.rect.x // 32
            y = self.rect.y // 32
            blocks = game.world.get_blocks_for_collision(x, y)

            self.rect.x += self.x_vel
            if self.rect.colliderect(game.world.map[205][11]):
                self.visible = False
                game.world.event.player_in_castle = True
            self.update_x_pos(blocks)

            self.rect.top += self.y_vel
            self.update_y_pos(blocks, game)

            # on_ground works incorrect without this piece of code
            x = self.rect.x // 32
            y = self.rect.y // 32
            if self.power_level > 0:
                y += 1
            for block in game.world.get_blocks_below(x, y):
                if block != 0 and block.type != 'BGObject':
                    if pygame.Rect(self.rect.x, self.rect.y + 1, self.rect.w, self.rect.h).colliderect(block.rect):
                        self.on_ground = True

        else:
            if game.world.flag.flag_rect.y + 20 > self.rect.y + self.rect.h:
                self.rect.y += 3

    def shoot_fireball(self, game, x, y, move_direction):
        game.world.spawn_fireball(x, y, move_direction)
        game.sound_manager.play('fireball', 0, 0.5)
        self.next_fireball_time = pygame.time.get_ticks() + 400

    def add_coins(self, count):
        self.coins += count

    def add_score(self, count):
        self.score += count

    def render(self, game):
        if self.visible:
            game.screen.blit(self.image, game.world.camera.apply(self))