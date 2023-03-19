import pygame

from const import GRAVITY, FALL_MULTIPLIER, STATE_LOADING


class Event:

    def __init__(self):

        # 0 - game over/kill
        # 1 - win
        self.type = 0

        self.delay = 0
        self.time = 0
        self.x_vel = 0
        self.y_vel = 0
        self.game_over = False

        self.player_in_castle = False
        self.tick = 0
        self.score_tick = 0

    def reset(self):
        self.type = 0

        self.delay = 0
        self.time = 0
        self.x_vel = 0
        self.y_vel = 0
        self.game_over = False

        self.player_in_castle = False
        self.tick = 0
        self.score_tick = 0

    def start_kill(self, game, game_over):
        """

        Player gets killed.

        """
        self.type = 0
        self.delay = 4000
        self.y_vel = -4
        self.time = pygame.time.get_ticks()
        self.game_over = game_over

        game.sound_manager.stop('overworld')
        game.sound_manager.stop('overworld_fast')
        game.sound_manager.play('death', 0, 0.5)

        # Sets "dead" sprite
        game.world.player.set_image(len(game.world.player.sprites))

    def start_win(self, game):
        self.type = 1
        self.delay = 2000
        self.time = 0

        game.sound_manager.stop('overworld')
        game.sound_manager.stop('overworld_fast')
        game.sound_manager.play('level_end', 0, 0.5)

        game.world.player.set_image(5)
        game.world.player.x_vel = 1
        game.world.player.rect.x += 10

        # Adding score depends on the map's time left.
        if game.world.time >= 300:
            game.world.player.add_score(5000)
            game.world.spawn_score_text(game.world.player.rect.x + 16, game.world.player.rect.y, score=5000)
        elif 200 <= game.world.time < 300:
            game.world.player.add_score(2000)
            game.world.spawn_score_text(game.world.player.rect.x + 16, game.world.player.rect.y, score=2000)
        else:
            game.world.player.add_score(1000)
            game.world.spawn_score_text(game.world.player.rect.x + 16, game.world.player.rect.y, score=1000)

    def update(self, game):

        # Death
        if self.type == 0:
            self.y_vel += GRAVITY * FALL_MULTIPLIER if self.y_vel < 6 else 0
            game.world.player.rect.y += self.y_vel

            if pygame.time.get_ticks() > self.time + self.delay:
                if not self.game_over:
                    game.world.player.reset_move()
                    game.world.player.reset_jump()
                    game.world.reset(False)
                    game.sound_manager.play('overworld', 9999999, 0.5)
                else:
                    game.menu_manager.current_state = STATE_LOADING
                    game.menu_manager.loading_menu.set_text_and_type('GAME OVER', False)
                    game.menu_manager.loading_menu.update_time()
                    game.sound_manager.play('game_over', 0, 0.5)

        # Flag win
        elif self.type == 1:

            if not self.player_in_castle:

                if not game.world.flag.flag_omitted:
                    game.world.player.set_image(5)
                    game.world.flag.move_flag_down()
                    game.world.player.flag_animation_move(game, False)

                else:
                    self.tick += 1
                    if self.tick == 1:
                        game.world.player.direction = False
                        game.world.player.set_image(6)
                        game.world.player.rect.x += 20
                    elif self.tick >= 30:
                        game.world.player.flag_animation_move(game, True)
                        game.world.player.update_image(game)

            else:
                if game.world.time > 0:
                    self.score_tick += 1
                    if self.score_tick % 10 == 0:
                        game.sound_manager.play('scorering', 0, 0.5)

                    game.world.time -= 1
                    game.world.player.add_score(50)

                else:
                    if self.time == 0:
                        self.time = pygame.time.get_ticks()

                    elif pygame.time.get_ticks() >= self.time + self.delay:
                        game.menu_manager.current_state = 'Loading'
                        game.menu_manager.loading_menu.set_text_and_type('LW1, PPOIS, Lipsky', False)
                        game.menu_manager.loading_menu.update_time()
                        game.sound_manager.play('game_over', 0, 0.5)
