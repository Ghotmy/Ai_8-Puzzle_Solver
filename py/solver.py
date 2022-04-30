# goal state is assumed to be 0123456789

import math
import queue

class Board:
    def solvable(self):
        b = self.board.replace('0', '')
        arr = [int(x) for x in b]
        n = len(arr)
        inv = 0

        for i in range(n * n):
            for j in range(i + 1, n):
                if (arr[i] > arr[j]):
                    inv += 1
    
        print(inv)
        return inv % 2 == 0

    def __init__(self, n, board):
        self.n = n
        self.board = board

    def goal(self):
        return self.board == '0123456789'
    
    def push(self, pzero, where):
        # simulate a tile push
        a = list(self.board)
        i = {'right': 1, 'left': -1, 'down': self.n, 'up': -self.n}.get(where)
        a[pzero], a[pzero+i] = a[pzero+i], a[pzero]
        return Board(self.n, ''.join(a))
    
    def neighbors(self):
        pos = self.board.find('0')
        nei = []

        if pos % self.n == 0: # first col
            nei.append(self.push(pos, 'right'))
        elif pos % (self.n - 1) == 0: # last col
            nei.append(self.push(pos, 'left'))
        else:
            nei.append(self.push(pos, 'right'))
            nei.append(self.push(pos, 'left'))

        if pos < self.n: # first row
            nei.append(self.push(pos, 'down'))
        elif pos >= (self.n * (self.n-1)): # last row
            nei.append(self.push(pos, 'up'))
        else:
            nei.append(self.push(pos, 'down'))
            nei.append(self.push(pos, 'up'))

        return nei

    def estimate_distance_to_goal(self, heuristic):
        dist = 0
        for i in range(self.n * self.n):
            p = self.board.find(str(i))
            if heuristic == 'hamming':
                dist += abs(i - p)
            else:
                diff_x = abs((i % self.n) - (p % self.n))
                diff_y = abs((i // self.n) - (p // self.n))

                if heuristic == 'manhattan':
                    dist += diff_x + diff_y
                elif heuristic == 'euclidean':
                    dist += math.sqrt(diff_x**2 + diff_y**2)
        return int(dist)

class Stack:
    def __init__(self):
        self.s = [] # stack
    def put(self, a):
        self.s.append(a)
    def get(self):
        self.s.pop()

class Solver:
    def __init__(self, initial):
        self.initial = initial
        self.solution = None

    def show_solution(self):
        if self.solution:
            #
            pass

    def solve(self, heuristic = None):
        # frontier should be an empty PriorityQueue
        # (a*) or Queue (bfs) or Stack (dfs),
        # heuristic is None if search is uninformed

        frontier = queue.PriorityQueue()
        frontier.put((0, self.initial))
        moves = 1
        # explored = set()

        while not frontier.empty():
            state = frontier.get()[1]
            # explored.put(state)

            if state.goal():
                self.solution = state
                return

            for nei in state.neighbors():
                frontier.put((moves, nei))

            moves += 1

