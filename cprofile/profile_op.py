import pstats
p = pstats.Stats('profile_algo.dat')
p.strip_dirs().sort_stats(-1).print_stats()
